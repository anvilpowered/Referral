package rocks.milspecsg.msreferral.api.member.repository;

import rocks.milspecsg.msreferral.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.repository.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberRepository<
    TKey,
    TDataStore> extends Repository<TKey, Member<TKey>, CacheService<TKey, Member<TKey>, TDataStore>, TDataStore> {

    CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateIfNotReferredForUser(UUID userUUID);

    CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(UUID userUUID);

    CompletableFuture<Boolean> addToReferredList(TKey memberId, UUID targetUserUUID);

    CompletableFuture<Boolean> parseAndAddToReferredList(Object memberId, UUID targetUserUUID);

    CompletableFuture<Boolean> setReferred(TKey memberId, boolean referred);
}
