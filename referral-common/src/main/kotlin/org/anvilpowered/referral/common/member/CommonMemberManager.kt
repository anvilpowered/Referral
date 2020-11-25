/*
 *   Referral - AnvilPowered
 *   Copyright (C) 2020
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.referral.common.member

import com.google.inject.Inject
import org.anvilpowered.anvil.api.command.CommandExecuteService
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.anvil.base.datastore.BaseManager
import org.anvilpowered.referral.api.member.MemberManager
import org.anvilpowered.referral.api.member.MemberRepository
import org.anvilpowered.referral.api.model.member.Member
import org.anvilpowered.referral.api.plugin.PluginMessages
import org.anvilpowered.referral.api.registry.ReferralKeys
import org.anvilpowered.referral.api.registry.Tier
import org.slf4j.Logger
import java.util.UUID
import java.util.concurrent.CompletableFuture

class CommonMemberManager<TUser, TPlayer, TString, TCommandSource> @Inject constructor(
    registry: Registry
) : BaseManager<MemberRepository<*, *>>(registry), MemberManager<TCommandSource> {

    @Inject
    private lateinit var commandExecuteService: CommandExecuteService

    @Inject
    private lateinit var logger: Logger

    @Inject
    private lateinit var pluginMessages: PluginMessages<TString>

    @Inject
    private lateinit var textService: TextService<TString, TCommandSource>

    @Inject
    private lateinit var userService: UserService<TUser, TPlayer>

    override fun info(source: TCommandSource, userUUID: UUID): CompletableFuture<Void> {
        return primaryComponent.getOneForUser(userUUID).thenAcceptAsync { member ->
            val userName = userService.getUserName(userUUID).join().orElse(userUUID.toString())
            if (member == null) {
                textService.builder()
                    .appendPrefix()
                    .red().append("Could not find ")
                    .gold().append(userName)
                    .sendTo(source)
                return@thenAcceptAsync
            }
            val builder = textService.builder()
                .append(
                    textService.builder()
                        .dark_gray().append("========= ")
                        .gold().append(userName)
                        .dark_gray().append(" =========")
                ).gray().append("\n\nTier: ")

            val tier = getTier(member.userUUID!!, member.referredUserUUIDs.size)
            if (tier.first == null) {
                builder.red().append("none")
            } else {
                builder.append(textService.deserialize(tier.first!!.name))
            }
            builder.gray().append("\n\nReferred by: ")
            if (member.referrerUserUUID == null) {
                builder.red().append("Not referred\n\n")
            } else {
                builder.aqua().append(
                    userService.getUserName(member.referrerUserUUID)
                        .join().orElse(member.referrerUserUUID.toString()), "\n\n"
                )
            }
            val referrals: List<UUID> = member.referredUserUUIDs
            builder.gray().append("Referrals (${referrals.size}): ")
            if (referrals.isEmpty()) {
                builder.red().append("No referrals")
            } else {
                builder.aqua().appendJoining(
                    ", ", *referrals.stream()
                        .map { userService.getUserName(it).join() }
                        .filter { it.isPresent }
                        .map { it.get() }
                        .toArray()
                )
            }
            builder.append(
                "\n\n", textService.builder()
                    .dark_gray().append("========= ")
                    .gold().appendPrefix()
                    .dark_gray().append("=========")
            ).sendTo(source)
        }
    }

    override fun refer(source: TCommandSource, userUUID: UUID, referrerUserUUID: UUID): CompletableFuture<Void> {
        if (userUUID == referrerUserUUID) {
            textService.send(pluginMessages.getSelfRefer(), source)
            return CompletableFuture.completedFuture(null)
        }
        return primaryComponent.getOneIfNotReferred(userUUID).thenAcceptAsync { member ->
            if (member == null) {
                textService.send(pluginMessages.getAlreadyReferred(), source)
                return@thenAcceptAsync
            }
            val userName = userService.getUserName(userUUID).join().orElse(userUUID.toString())
            val referrerUserName = userService.getUserName(referrerUserUUID).join().orElse(referrerUserUUID.toString())
            if (!primaryComponent.refer(userUUID, referrerUserUUID).join()) {
                textService.send(pluginMessages.getFailedReferral(referrerUserName), source)
                return@thenAcceptAsync
            }
            // if we got here, the referral was successful
            checkTier(referrerUserUUID, userName, member.referredUserUUIDs.size + 1)
            userService.getPlayer(referrerUserUUID).ifPresent { player ->
                textService.send(pluginMessages.getReferredSuccess(userName), player as TCommandSource)
            }
            textService.send(pluginMessages.getReferralSuccess(referrerUserName), source)
        }.exceptionally { it.printStackTrace(); null }
    }

    override fun top(source: TCommandSource): CompletableFuture<Void> {
        return primaryComponent.top().thenAccept { members ->
            textService.paginationBuilder()
                .contents(members.map { line(it) })
        }
    }

    private fun line(member: Member<*>): TString {
        return textService.builder()
            .gold().append(userService.getUserName(member.userUUID).join().orElse("none"))
            .build()
    }

    private fun checkTier(referrerUserUUID: UUID, lastReferredUserName: String, referralCount: Int) {
        val commands = registry.getOrDefault(ReferralKeys.EACH_REFERRAL_COMMANDS).toMutableList()
        val tier = getTier(referrerUserUUID, referralCount)
        if (tier.second && tier.first != null) {
            commands.addAll(registry.getOrDefault(ReferralKeys.EACH_TIER_COMMANDS))
            commands.addAll(tier.first!!.commands)
        }
        for ((index, command) in commands.withIndex()) {
            commands[index] = command
                .replace("%player%", userService.getUserName(referrerUserUUID).join().orElse("null"))
                .replace("%tier%", tier.first?.name ?: "none")
                .replace("%lastref%", lastReferredUserName)
                .replace("%refcount%", referralCount.toString())
        }
        // only run commands if player is online
        if (userService.getPlayer(referrerUserUUID).isPresent) {
            for (command in commands) {
                commandExecuteService.execute(command)
            }
        } else if (!primaryComponent.addQueuedCommands(referrerUserUUID, commands).join()) {
            logger.error("Failed to add queuedCommands to entity for user {}", referrerUserUUID)
        }
    }

    private fun getTier(userUUID: UUID, referralCount: Int): Pair<Tier?, Boolean> {
        var matchingTier: Tier? = null
        var justReached = false
        for (tier in registry.getOrDefault(ReferralKeys.TIERS)) {
            if (referralCount >= tier.referralRequirement
                && tier.referralRequirement >= matchingTier?.referralRequirement ?: Int.MIN_VALUE
            ) {
                matchingTier = tier
                if (tier.referralRequirement == referralCount) {
                    justReached = true
                }
            }
        }
        return Pair(matchingTier, justReached)
    }
}
