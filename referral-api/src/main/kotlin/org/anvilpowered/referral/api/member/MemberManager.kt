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

import org.anvilpowered.anvil.api.datastore.Manager
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface MemberManager<TCommandSource> : Manager<MemberRepository<*, *>> {
    override fun getDefaultIdentifierSingularUpper(): String = "Member"
    override fun getDefaultIdentifierPluralUpper(): String = "Members"
    override fun getDefaultIdentifierSingularLower(): String = "member"
    override fun getDefaultIdentifierPluralLower(): String = "members"

    fun info(source: TCommandSource, userUUID: UUID): CompletableFuture<Void>
    fun refer(source: TCommandSource, userUUID: UUID, referrerUserUUID: UUID): CompletableFuture<Void>
    fun top(source: TCommandSource): CompletableFuture<Void>
}
