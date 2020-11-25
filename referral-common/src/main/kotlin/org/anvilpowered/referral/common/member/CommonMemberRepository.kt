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
import org.anvilpowered.anvil.base.datastore.BaseRepository
import org.anvilpowered.referral.api.member.MemberRepository
import org.anvilpowered.referral.api.model.member.Member
import org.slf4j.Logger
import java.util.UUID
import java.util.concurrent.CompletableFuture

abstract class CommonMemberRepository<TKey, TDataStore>
    : BaseRepository<TKey, Member<TKey>, TDataStore>(), MemberRepository<TKey, TDataStore> {

    @Inject
    protected lateinit var commandExecuteService: CommandExecuteService

    @Inject
    protected lateinit var logger: Logger

    @Suppress("UNCHECKED_CAST")
    override fun getTClass(): Class<Member<TKey>> {
        return dataStoreContext.getEntityClassUnsafe("member") as Class<Member<TKey>>
    }

    private fun getOneForUser(userUUID: UUID, mapper: (Member<TKey>) -> Member<TKey>?): CompletableFuture<Member<TKey>?> {
        return getOneForUser(userUUID).thenApplyAsync { member ->
            if (member != null) {
                return@thenApplyAsync mapper(member)
            }
            // if not present, generate a new one
            val newMember = generateEmpty()
            newMember.userUUID = userUUID
            insertOne(newMember).join().orElse(null)
        }
    }

    override fun getOneIfNotReferred(userUUID: UUID): CompletableFuture<Member<TKey>?> {
        return getOneForUser(userUUID) { if (it.referrerUserUUID == null) it else null }
    }

    override fun onPlayerJoin(userUUID: UUID): CompletableFuture<Member<TKey>?> {
        return getOneForUser(userUUID) { it }.thenApplyAsync {
            if (it != null) {
                for (command in it.queuedCommands) {
                    commandExecuteService.execute(command)
                }
            }
            it
        }
    }
}
