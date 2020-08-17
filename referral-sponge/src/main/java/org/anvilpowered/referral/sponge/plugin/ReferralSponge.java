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

package org.anvilpowered.referral.sponge.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.referral.api.ReferralImpl;
import org.anvilpowered.referral.common.plugin.ReferralPluginInfo;
import org.anvilpowered.referral.sponge.listener.SpongePlayerListener;
import org.anvilpowered.referral.sponge.module.SpongeModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = ReferralPluginInfo.id,
    name = ReferralPluginInfo.name,
    version = ReferralPluginInfo.version,
    description = ReferralPluginInfo.description,
    dependencies = @Dependency(id = "anvil"),
    url = ReferralPluginInfo.url,
    authors = "Cableguy20"
)
public class ReferralSponge extends ReferralImpl {

    @Inject
    public ReferralSponge(Injector injector) {
        super(injector, new SpongeModule());
    }

    @Override
    protected void whenReady(Environment environment) {
        super.whenReady(environment);
        Sponge.getEventManager().registerListeners(this,
            environment.getInjector().getInstance(SpongePlayerListener.class));
    }
}
