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

package org.anvilpowered.referral.spigot

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import org.anvilpowered.anvil.api.Environment
import org.anvilpowered.referral.common.ReferralImpl
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class ReferralSpigot : JavaPlugin() {

    private val inner: Inner

    init {
        val module = object : AbstractModule() {
            override fun configure() {
                bind(Plugin::class.java).toInstance(this@ReferralSpigot)
                bind(JavaPlugin::class.java).toInstance(this@ReferralSpigot)
                bind(ReferralSpigot::class.java).toInstance(this@ReferralSpigot)
            }
        }
        val injector = Guice.createInjector(module)
        inner = Inner(injector)
    }

    private inner class Inner(injector: Injector) : ReferralImpl(injector, SpigotModule()) {
        override fun applyToBuilder(builder: Environment.Builder) {
            super.applyToBuilder(builder)
            builder.addEarlyServices(SpigotPlayerListener::class.java) {
                Bukkit.getPluginManager().registerEvents(it, this@ReferralSpigot)
            }
        }
    }
}
