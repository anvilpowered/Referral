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

package org.anvilpowered.referral.common.command

import com.google.inject.Inject
import org.anvilpowered.anvil.api.asNullable
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.referral.api.member.MemberManager
import org.anvilpowered.referral.api.plugin.PluginMessages
import org.anvilpowered.referral.api.registry.ReferralKeys

open class CommonByCommand<
    TUser : Any,
    TPlayer : Any,
    TString : Any,
    TCommandSource : Any
    >(private val playerClass: Class<TPlayer>) {

    @Inject
    private lateinit var memberManager: MemberManager<TCommandSource>

    @Inject
    private lateinit var permissionService: PermissionService

    @Inject
    private lateinit var pluginMessages: PluginMessages<TString>

    @Inject
    protected lateinit var registry: Registry

    @Inject
    private lateinit var textService: TextService<TString, TCommandSource>

    @Inject
    private lateinit var userService: UserService<TUser, TPlayer>

    private val runAsPlayer: TString by lazy {
        textService.builder()
            .appendPrefix()
            .red().append("Must run as player!")
            .build()
    }

    private val usage: TString by lazy {
        textService.builder()
            .appendPrefix()
            .red().append("Usage: ${CommonReferralCommandNode.BY_USAGE}")
            .build()
    }

    private fun testPermission(source: Any?): Boolean {
        return permissionService.hasPermission(source, registry.getOrDefault(ReferralKeys.BY_PERMISSION))
    }

    private fun hasNoPerms(source: TCommandSource): Boolean {
        if (!testPermission(source)) {
            textService.send(pluginMessages.getNoPermission(), source)
            return true
        }
        return false
    }

    open fun execute(source: TCommandSource, context: Array<String>) {
        if (hasNoPerms(source)) return
        if (!playerClass.isAssignableFrom(source::class.java)) {
            textService.send(runAsPlayer, source)
            return
        }
        if (context.size != 1) {
            textService.send(usage, source)
            return
        }
        userService.getUUID(context[0]).asNullable().thenApplyAsync {
            if (it == null) {
                textService.send(pluginMessages.getPlayerNotFound(context[0]), source)
            } else {
                memberManager.refer(source, userService.getUUID(source as TUser), it).join()
            }
        }
    }

    open fun suggest(source: TCommandSource, context: Array<String>): List<String> {
        if (!testPermission(source)) return listOf()
        return userService.matchPlayerNames(context, 0, 1)
    }
}
