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

import com.google.inject.TypeLiteral
import net.md_5.bungee.api.chat.TextComponent
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.anvilpowered.anvil.api.command.CommandNode
import org.anvilpowered.referral.common.CommonModule
import org.anvilpowered.referral.common.ReferralPluginInfo
import org.anvilpowered.referral.spigot.command.SpigotReferralCommandNode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.nio.file.Paths

class SpigotModule : CommonModule<Player, Player, TextComponent, CommandSender>() {
    override fun configure() {
        super.configure()
        bind(object : TypeLiteral<CommandNode<CommandSender>>() {}).to(SpigotReferralCommandNode::class.java)
        val configFilesLocation = Paths.get("plugins/" + ReferralPluginInfo.id).toFile()
        if (!configFilesLocation.exists()) {
            check(configFilesLocation.mkdirs()) { "Unable to create config directory" }
        }
        bind(object : TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {}).toInstance(
            HoconConfigurationLoader.builder().setPath(Paths.get("$configFilesLocation/referral.conf")).build()
        )
    }
}
