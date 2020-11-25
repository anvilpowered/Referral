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

package org.anvilpowered.referral.common.model.member

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.EntityId
import jetbrains.exodus.util.ByteArraySizedInputStream
import org.anvilpowered.anvil.api.datastore.XodusEntity
import org.anvilpowered.anvil.api.model.Mappable
import org.anvilpowered.anvil.base.model.XodusDbo
import org.anvilpowered.referral.api.model.member.Member
import java.io.IOException
import java.util.UUID

@XodusEntity
class XodusMember : XodusDbo(), Member<EntityId>, Mappable<Entity> {

    override var userUUID: UUID? = null
    override var referrerUserUUID: UUID? = null
    override var referredUserUUIDs: MutableList<UUID> = mutableListOf()
    override var queuedCommands: MutableList<String> = mutableListOf()

    override fun writeTo(entity: Entity): Entity {
        super.writeTo(entity)
        if (userUUID != null) {
            entity.setProperty("userUUID", userUUID.toString())
        }
        if (referrerUserUUID != null) {
            entity.setProperty("referrerUserUUID", referrerUserUUID.toString())
        }
        try {
            entity.setBlob(
                "referredUserUUIDs",
                ByteArraySizedInputStream(Mappable.serializeUnsafe(referredUserUUIDs))
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            entity.setBlob(
                "queuedCommands",
                ByteArraySizedInputStream(Mappable.serializeUnsafe(queuedCommands))
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return entity
    }

    override fun readFrom(entity: Entity) {
        super.readFrom(entity)
        val userUUID = entity.getProperty("userUUID")
        if (userUUID is String) {
            this.userUUID = UUID.fromString(userUUID)
        }
        val referrerUserUUID = entity.getProperty("referrerUserUUID")
        if (referrerUserUUID is String) {
            this.referrerUserUUID = UUID.fromString(referrerUserUUID)
        }
        Mappable.deserialize<MutableList<UUID>>(entity.getBlob("referredUserUUIDs"))
            .ifPresent { referredUserUUIDs = it }
        Mappable.deserialize<MutableList<String>>(entity.getBlob("queuedCommands"))
            .ifPresent { queuedCommands = it }
    }
}
