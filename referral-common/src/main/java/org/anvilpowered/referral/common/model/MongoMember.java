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

package org.anvilpowered.referral.common.model;

import org.anvilpowered.anvil.base.model.MongoDbo;
import org.anvilpowered.referral.api.model.Member;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity("members")
public class MongoMember extends MongoDbo implements Member<ObjectId> {

    private UUID userUUID;
    private UUID referrerUserUUID;
    private List<UUID> referredUserUUIDs;

    @Override
    public UUID getUserUUID() {
        return userUUID;
    }

    @Override
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    @Override
    public UUID getReferrerUserUUID() {
        return referrerUserUUID;
    }

    @Override
    public void setReferrerUserUUID(UUID referrerUserUUID) {
        this.referrerUserUUID = referrerUserUUID;
    }

    @Override
    public List<UUID> getReferredUserUUIDs() {
        if (referredUserUUIDs == null) {
            referredUserUUIDs = new ArrayList<>();
        }
        return referredUserUUIDs;
    }

    @Override
    public void setReferredUserUUIDs(List<UUID> referredUserUUIDs) {
        this.referredUserUUIDs = referredUserUUIDs;
    }
}
