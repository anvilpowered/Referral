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

import dev.morphia.Datastore
import dev.morphia.query.Query
import org.anvilpowered.anvil.api.asNullable
import org.anvilpowered.anvil.base.datastore.BaseMongoRepository
import org.anvilpowered.referral.api.member.MongoMemberRepository
import org.anvilpowered.referral.api.model.member.Member
import org.bson.types.ObjectId
import java.util.UUID
import java.util.concurrent.CompletableFuture

class CommonMongoMemberRepository
    : CommonMemberRepository<ObjectId, Datastore>(), BaseMongoRepository<Member<ObjectId>>, MongoMemberRepository {

    override fun getOneForUser(userUUID: UUID): CompletableFuture<Member<ObjectId>?> {
        return getOne(asQuery(userUUID)).asNullable()
    }

    override fun addQueuedCommands(userUUID: UUID, commands: List<String>): CompletableFuture<Boolean> {
        return update(asQuery(userUUID), createUpdateOperations().addToSet("queuedCommands", commands))
    }

    override fun clearQueuedCommands(userUUID: UUID): CompletableFuture<Boolean> {
        return update(asQuery(userUUID), createUpdateOperations().unset("queuedCommands"))
    }

    override fun refer(userUUID: UUID, referrerUserUUID: UUID): CompletableFuture<Boolean> {
        val query1 = asQuery(userUUID)
        val query2 = asQuery(referrerUserUUID)
        return update(query1, set("referrerUserUUID", referrerUserUUID)).thenCombine(
            update(query2, createUpdateOperations().addToSet("referredUserUUIDs", userUUID))
        ) { r1, r2 ->
            when {
                r1 && r2 -> return@thenCombine true
                !r1 && !r2 -> {
                    logger.error("Failed to add uuid to referredUserUUIDs for user $referrerUserUUID and set referrerUserUUID " +
                        "for user $userUUID")
                    return@thenCombine false
                }
            }
            if (r1) {
                if (update(query1, unSet("referrerUserUUID")).join()) {
                    logger.error("Failed to add uuid to referredUserUUIDs for user $referrerUserUUID but successfully reverted " +
                        "referrerUserUUID for user $userUUID")
                } else {
                    logger.error("Failed to add uuid to referredUserUUIDs for user $referrerUserUUID and was unable to revert" +
                        "referrerUserUUID for user $userUUID")
                }
            }
            if (r2) {
                if (update(query2, createUpdateOperations().removeAll("referredUserUUIDs", userUUID)).join()) {
                    logger.error("Failed to set referrerUserUUID for user $userUUID but successfully reverted" +
                        "referredUserUUIDs for user $referrerUserUUID")
                } else {
                    logger.error("Failed to set referrerUserUUID for user $userUUID and was unable to revert" +
                        "referredUserUUIDs for user $referrerUserUUID")
                }
            }
            false
        }
    }

    override fun top(): CompletableFuture<Iterable<Member<ObjectId>>> {
        /*
        dataStoreContext.dataStore.createAggregation(tClass)
            .project(Projection.projection(""))
            .project(Group.grouping("referCount", Accumulator.accumulator("\$size", "referredUserUUIDs")))
            .sort(Sort.descending("referCount"))
        */
        return CompletableFuture.completedFuture(listOf())
    }

    override fun asQuery(uuid: UUID): Query<Member<ObjectId>> {
        return asQuery().field("userUUID").equal(uuid)
    }
}
