/*
 *   OnTime - AnvilPowered
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

import com.google.inject.TypeLiteral
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.TextComponent
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.anvilpowered.anvil.api.command.CommandNode
import org.anvilpowered.referral.common.CommonModule
import org.anvilpowered.referral.common.ReferralPluginInfo
import org.anvilpowered.referral.velocity.command.VelocityReferralCommandNode
import java.nio.file.Paths

class VelocityModule : CommonModule<Player, Player, TextComponent, CommandSource>() {
    override fun configure() {
        super.configure()
        bind(object : TypeLiteral<CommandNode<CommandSource>>() {}).to(VelocityReferralCommandNode::class.java)
        val configFilesLocation = Paths.get("plugins/" + ReferralPluginInfo.id).toFile()
        if (!configFilesLocation.exists()) {
            check(configFilesLocation.mkdirs()) { "Unable to create config directory" }
        }
        bind(object : TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {}).toInstance(
            HoconConfigurationLoader.builder().setPath(Paths.get("$configFilesLocation/referral.conf")).build()
        )
    }
}
