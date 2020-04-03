/*
 *   Referral - AnvilPowered
 *   Copyright (C) 2020 Cableguy20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.referral.common.member.repository;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.datastore.DataStoreContext;
import org.anvilpowered.anvil.base.repository.BaseMongoRepository;
import org.anvilpowered.referral.api.member.repository.MongoMemberRepository;
import org.anvilpowered.referral.api.model.member.Member;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMongoMemberRepository
    extends CommonMemberRepository<ObjectId, Datastore>
    implements BaseMongoRepository<Member<ObjectId>>,
    MongoMemberRepository {

    @Inject
    protected CommonMongoMemberRepository(DataStoreContext<ObjectId, Datastore> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(asQuery(userUUID).get()));
    }

    @Override
    public Query<Member<ObjectId>> asQuery(UUID userUUID) {
        return asQuery().field("userUUID").equal(userUUID);
    }

    @Override
    public CompletableFuture<Boolean> refer(UUID userUUID, UUID referrerUserUUID) {
        return update(asQuery(userUUID), set("referrerUserUUID", referrerUserUUID))
            .thenApplyAsync(result -> result && update(asQuery(referrerUserUUID),
                createUpdateOperations().addToSet("referredUserUUIDs", userUUID)).join());
    }
}
