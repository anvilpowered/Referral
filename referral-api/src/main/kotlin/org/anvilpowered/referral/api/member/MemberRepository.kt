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

package org.anvilpowered.referral.api.member

import org.anvilpowered.anvil.api.datastore.Repository
import org.anvilpowered.referral.api.model.member.Member
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface MemberRepository<TKey, TDataStore> : Repository<TKey, Member<TKey>, TDataStore> {

    fun getOneIfNotReferred(userUUID: UUID): CompletableFuture<Member<TKey>?>

    fun onPlayerJoin(userUUID: UUID): CompletableFuture<Member<TKey>?>

    fun getOneForUser(userUUID: UUID): CompletableFuture<Member<TKey>?>

    fun addQueuedCommands(userUUID: UUID, commands: List<String>): CompletableFuture<Boolean>

    fun clearQueuedCommands(userUUID: UUID): CompletableFuture<Boolean>

    fun refer(userUUID: UUID, referrerUserUUID: UUID): CompletableFuture<Boolean>

    fun top(): CompletableFuture<Iterable<Member<TKey>>>
}
