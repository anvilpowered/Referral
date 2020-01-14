package rocks.milspecsg.msreferral.model.core.member;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msrepository.common.model.MongoDbo;

import java.util.List;
import java.util.UUID;

@Entity("members")
public class MongoMember extends MongoDbo implements Member<ObjectId> {

    private UUID userUUID;
    private boolean referred;
    private List<ObjectId> referredUserIds;

    @Override
    public UUID getUserUUID() {
        return userUUID;
    }

    @Override
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    @Override
    public List<ObjectId> getReferredUserIds() {
        return referredUserIds;
    }

    @Override
    public void setReferredUserIds(List<ObjectId> referredUserUUIDs) {
        this.referredUserIds = referredUserUUIDs;
    }

    @Override
    public boolean isReferred() {
        return referred;
    }

    @Override
    public void setReferred(boolean referred) {
        this.referred = referred;
    }

}
