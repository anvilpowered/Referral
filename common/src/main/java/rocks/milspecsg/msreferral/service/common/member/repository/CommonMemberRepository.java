package rocks.milspecsg.msreferral.service.common.member.repository;

import rocks.milspecsg.msreferral.api.member.repository.MemberRepository;
import rocks.milspecsg.msreferral.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.common.repository.CommonRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class CommonMemberRepository<
    TKey,
    TDataStore>
    extends CommonRepository<TKey, Member<TKey>, CacheService<TKey, Member<TKey>, TDataStore>, TDataStore>
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
                if(member.isReferred()) {
                    return Optional.empty();
                }
                if(setReferred(member.getId(), true).join()) {
                    member.setReferred(true);
                    return Optional.of(member);
                } else {
                    return Optional.empty();
                }
            }
            //If one isn't present, generate a new one
            Member<TKey> member = generateEmpty();
            member.setUserUUID(userUUID);
            member.setReferred(true);
            return insertOne(member).join();
        });
    }

    @Override
    public CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID) {
        return getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                return optionalMember;
            }
            //If one isn't present, generate a new one
            Member<TKey> member = generateEmpty();
            member.setUserUUID(userUUID);
            return insertOne(member).join();
        });
    }
}
