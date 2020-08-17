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

package org.anvilpowered.referral.common.module;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.anvilpowered.anvil.api.Anvil;
import org.anvilpowered.anvil.api.registry.ConfigurationService;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.misc.BindingExtensions;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.referral.api.member.MemberManager;
import org.anvilpowered.referral.api.member.MemberRepository;
import org.anvilpowered.referral.common.registry.CommonConfigurationService;
import org.anvilpowered.referral.common.registry.ReferralRegistry;
import org.anvilpowered.referral.common.member.CommonMemberManager;
import org.anvilpowered.referral.common.member.CommonMongoMemberRepository;
import org.anvilpowered.referral.common.plugin.ReferralPluginInfo;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

@SuppressWarnings("UnstableApiUsage")
public class CommonModule<
    TUser,
    TPlayer,
    TString,
    TCommandSource>
    extends AbstractModule {

    @Override
    protected void configure() {
        BindingExtensions be = Anvil.getBindingExtensions(binder());

        be.bind(
            new TypeToken<MemberRepository<?, ?>>(getClass()) {
            },
            new TypeToken<MemberRepository<ObjectId, Datastore>>(getClass()) {
            },
            new TypeToken<CommonMongoMemberRepository>(getClass()) {
            },
            Names.named("mongodb")
        );

        be.bind(
            new TypeToken<MemberManager<TString>>(getClass()) {
            },
            new TypeToken<CommonMemberManager<TUser, TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<PluginInfo<TString>>(getClass()) {
            },
            new TypeToken<ReferralPluginInfo<TString, TCommandSource>>(getClass()) {
            }
        );

        be.withMongoDB();

        bind (ConfigurationService.class).to(CommonConfigurationService.class);
        bind(Registry.class).to(ReferralRegistry.class);
    }
}
