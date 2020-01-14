package rocks.milspecsg.msreferral.model.core.member;


import rocks.milspecsg.msrepository.api.model.ObjectWithId;

import java.util.List;
import java.util.UUID;

public interface Member<TKey> extends ObjectWithId<TKey> {

    UUID getUserUUID();
    void setUserUUID(UUID userUUID);

    List<TKey> getReferredUserIds();
    void setReferredUserIds(List<TKey> referredUserUUIDs);

    boolean isReferred();
    void setReferred(boolean referred);
}
