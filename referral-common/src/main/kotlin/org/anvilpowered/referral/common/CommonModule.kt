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

package org.anvilpowered.referral.common

import com.google.common.reflect.TypeToken
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import dev.morphia.Datastore
import jetbrains.exodus.entitystore.EntityId
import jetbrains.exodus.entitystore.PersistentEntityStore
import org.anvilpowered.anvil.api.Anvil
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.registry.ConfigurationService
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.referral.api.member.MemberManager
import org.anvilpowered.referral.api.member.MemberRepository
import org.anvilpowered.referral.api.plugin.PluginMessages
import org.anvilpowered.referral.common.member.CommonMemberManager
import org.anvilpowered.referral.common.member.CommonMongoMemberRepository
import org.anvilpowered.referral.common.member.CommonXodusMemberRepository
import org.anvilpowered.referral.common.registry.CommonConfigurationService
import org.anvilpowered.referral.common.registry.CommonRegistry
import org.bson.types.ObjectId
import plugin.CommonPluginMessages

@Suppress("UnstableApiUsage")
open class CommonModule<
    TUser : Any,
    TPlayer : Any,
    TString : Any,
    TCommandSource : Any,
    > : AbstractModule() {

    override fun configure() {
        val be = Anvil.getBindingExtensions(binder())

        be.bind(
            object : TypeToken<PluginMessages<TString>>(javaClass) {},
            object : TypeToken<CommonPluginMessages<TString, TCommandSource>>(javaClass) {}
        )

        be.bind(
            object : TypeToken<MemberRepository<*, *>>(javaClass) {},
            object : TypeToken<MemberRepository<ObjectId, Datastore>>(javaClass) {},
            object : TypeToken<CommonMongoMemberRepository>(javaClass) {},
            Names.named("mongodb")
        )

        be.bind(
            object : TypeToken<MemberRepository<*, *>>(javaClass) {},
            object : TypeToken<MemberRepository<EntityId, PersistentEntityStore>>(javaClass) {},
            object : TypeToken<CommonXodusMemberRepository>(javaClass) {},
            Names.named("xodus")
        )

        be.bind(
            object : TypeToken<MemberManager<TCommandSource>>(javaClass) {},
            object : TypeToken<CommonMemberManager<TUser, TPlayer, TString, TCommandSource>>(javaClass) {}
        )

        be.bind(
            object : TypeToken<PluginInfo<TString>>(javaClass) {},
            object : TypeToken<ReferralPluginInfo<TString, TCommandSource>>(javaClass) {}
        )

        be.withMongoDB()
        be.withXodus()

        bind(ConfigurationService::class.java).to(CommonConfigurationService::class.java)
        bind(Registry::class.java).to(CommonRegistry::class.java)
    }
}
