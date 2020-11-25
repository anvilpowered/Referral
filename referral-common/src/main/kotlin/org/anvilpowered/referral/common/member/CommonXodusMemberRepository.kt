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

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.EntityId
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.StoreTransaction
import org.anvilpowered.anvil.api.asNullable
import org.anvilpowered.anvil.api.model.Mappable
import org.anvilpowered.anvil.base.datastore.BaseXodusRepository
import org.anvilpowered.referral.api.member.XodusMemberRepository
import org.anvilpowered.referral.api.model.member.Member
import java.util.UUID
import java.util.concurrent.CompletableFuture

class CommonXodusMemberRepository
    : CommonMemberRepository<EntityId, PersistentEntityStore>(), BaseXodusRepository<Member<EntityId>>, XodusMemberRepository {

    override fun getOneForUser(userUUID: UUID): CompletableFuture<Member<EntityId>?> {
        return getOne(asQuery(userUUID)).asNullable()
    }

    override fun addQueuedCommands(userUUID: UUID, commands: List<String>): CompletableFuture<Boolean> {
        return update(asQuery(userUUID)) { entity ->
            if (!Mappable.addToCollection(
                    entity.getBlob("queuedCommands"),
                    { entity.setBlob("queuedCommands", it) },
                    commands
                )
            ) {
                logger.error("Failed to add queuedCommands to entity for user {}", userUUID)
            }
        }
    }

    override fun refer(userUUID: UUID, referrerUserUUID: UUID): CompletableFuture<Boolean> {
        val query1 = asQuery(userUUID)
        val query2 = asQuery(referrerUserUUID)
        return update(query1) { it.setProperty("referrerUserUUID", referrerUserUUID.toString()) }.thenCombine(
            update(asQuery(referrerUserUUID)) { entity ->
                if (!Mappable.addToCollection(
                        entity.getBlob("referredUserUUIDs"),
                        { entity.setBlob("referredUserUUIDs", it) },
                        userUUID
                    )
                ) {
                    logger.error("Failed to add referredUserUUID to entity for user {}", userUUID)
                }
            }
        ) { r1, r2 ->
            when {
                r1 && r2 -> return@thenCombine true
                !r1 && !r2 -> return@thenCombine false
            }
            if (r1) {
                update(query1) { it.deleteProperty("referrerUserUUID") }
            }
            if (r2) {
                update(query2) { entity ->
                    if (!Mappable.removeFromCollection(
                            entity.getBlob("referredUserUUIDs"),
                            { entity.setBlob("referredUserUUIDs", it) },
                            referrerUserUUID::equals
                        )
                    ) {
                        logger.error("Failed to add referredUserUUID to entity for user {}", userUUID)
                    }
                }
            }
            false
        }
    }

    override fun top(): CompletableFuture<Iterable<Member<EntityId>>> {
        return CompletableFuture.supplyAsync {
            dataStoreContext.dataStore.computeInReadonlyTransaction { txn ->
                txn.getAll(tClass.simpleName).asSequence().map {
                    val item = generateEmpty()
                    (item as Mappable<Entity>).readFrom(it)
                    item
                }.asIterable()
            }
        }
    }

    override fun asQuery(userUUID: UUID): (StoreTransaction) -> Iterable<Entity> {
        return { txn -> txn.find(tClass.simpleName, "userUUID", userUUID.toString()) }
    }
}
