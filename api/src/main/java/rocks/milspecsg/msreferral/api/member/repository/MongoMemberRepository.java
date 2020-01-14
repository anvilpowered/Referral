package rocks.milspecsg.msreferral.api.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import rocks.milspecsg.msreferral.model.core.member.Member;

import java.util.Optional;
import java.util.UUID;

public interface MongoMemberRepository extends MemberRepository<ObjectId, Datastore> {

    Optional<? extends Query<Member<ObjectId>>> asQuery(UUID userUUID);
}
