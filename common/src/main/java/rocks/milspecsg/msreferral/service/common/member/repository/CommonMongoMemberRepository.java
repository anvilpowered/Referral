package rocks.milspecsg.msreferral.service.common.member.repository;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import rocks.milspecsg.msreferral.api.member.repository.MongoMemberRepository;
import rocks.milspecsg.msreferral.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.common.repository.CommonMongoRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMongoMemberRepository
    extends CommonMemberRepository<ObjectId, Datastore>
    implements CommonMongoRepository<Member<ObjectId>, CacheService<ObjectId, Member<ObjectId>, Datastore>>,
    MongoMemberRepository {

    @Inject
    protected CommonMongoMemberRepository(DataStoreContext<ObjectId, Datastore> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    public Optional<Query<Member<ObjectId>>> asQuery(UUID userUUID) {
        return asQuery().map(q -> q.field("userUUID").equal(userUUID));
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> asQuery(userUUID).map(QueryResults::get));
    }

    @Override
    public CompletableFuture<Boolean> addToReferredList(ObjectId memberId, UUID targetUserUUID) {
        return getOneOrGenerateForUser(targetUserUUID).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return false;
            }
            return createUpdateOperations().map(u -> u.addToSet("referredUserIds", memberId))
                .map(u -> getDataStoreContext().getDataStore()
                    .map(dataStore -> asQuery(targetUserUUID)
                        .map(q -> dataStore.update(q, u).getUpdatedCount() > 0)
                        .orElse(false))
                    .orElse(false)
                ).orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> parseAndAddToReferredList(Object memberId, UUID targetUserUUID) {
        return addToReferredList(parseUnsafe(memberId), targetUserUUID).exceptionally(e -> false);
    }

    @Override
    public CompletableFuture<Boolean> setReferred(ObjectId memberId, boolean referred) {
        return CompletableFuture.supplyAsync(() ->
            createUpdateOperations().map(u -> u.set("referred", referred))
                .map(u -> getDataStoreContext().getDataStore()
                    .map(dataStore -> asQuery(memberId)
                        .map(q -> dataStore.update(q, u).getUpdatedCount() > 0)
                        .orElse(false))
                    .orElse(false)
                ).orElse(false)
        );
    }
}
