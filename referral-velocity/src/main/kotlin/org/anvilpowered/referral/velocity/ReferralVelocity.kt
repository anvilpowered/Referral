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

package org.anvilpowered.referral.velocity

import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.api.Environment
import org.anvilpowered.referral.common.ReferralImpl
import org.anvilpowered.referral.common.ReferralPluginInfo

@Plugin(
    id = ReferralPluginInfo.id,
    name = ReferralPluginInfo.name,
    version = ReferralPluginInfo.version,
    dependencies = [Dependency(id = "anvil")],
    description = ReferralPluginInfo.description,
    url = ReferralPluginInfo.url,
    authors = ["Cableguy20"]
)
class ReferralVelocity @Inject constructor(
    injector: Injector,
    private val proxyServer: ProxyServer,
) : ReferralImpl(injector, VelocityModule()) {

    override fun applyToBuilder(builder: Environment.Builder) {
        super.applyToBuilder(builder)
        builder.addEarlyServices(VelocityPlayerListener::class.java) {
            proxyServer.eventManager.register(this, it)
        }
    }
}
