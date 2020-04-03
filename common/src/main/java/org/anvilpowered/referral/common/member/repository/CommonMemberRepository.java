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

import org.anvilpowered.anvil.api.datastore.DataStoreContext;
import org.anvilpowered.anvil.base.repository.BaseRepository;
import org.anvilpowered.referral.api.member.repository.MemberRepository;
import org.anvilpowered.referral.api.model.member.Member;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class CommonMemberRepository<
    TKey,
    TDataStore>
    extends BaseRepository<TKey, Member<TKey>, TDataStore>
    implements MemberRepository<TKey, TDataStore> {

    protected CommonMemberRepository(DataStoreContext<TKey, TDataStore> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Member<TKey>> getTClass() {
        return (Class<Member<TKey>>) getDataStoreContext().getEntityClassUnsafe("member");
    }

    @Override
    public CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateIfNotReferredForUser(UUID userUUID) {
        return getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                Member<TKey> member = optionalMember.get();
                if (member.getReferrerUserUUID() != null) {
                    return Optional.empty();
                }
                return optionalMember;
            }
            // if not present, generate a new one
            Member<TKey> member = generateEmpty();
            member.setUserUUID(userUUID);
            return insertOne(member).join();
        });
    }

    @Override
    public CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID) {
        return getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                return optionalMember;
            }
            // if not present, generate a new one
            Member<TKey> member = generateEmpty();
            member.setUserUUID(userUUID);
            return insertOne(member).join();
        });
    }
}
