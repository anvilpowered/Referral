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

package org.anvilpowered.referral.spigot.command

import com.google.inject.Inject
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.referral.common.command.CommonReferralCommandNode
import org.anvilpowered.referral.spigot.ReferralSpigot
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SpigotReferralCommandNode @Inject constructor(
    registry: Registry
) : CommonReferralCommandNode<CommandExecutor, CommandSender>(registry) {

    @Inject
    private lateinit var byCommand: SpigotByCommand

    @Inject
    private lateinit var infoCommand: SpigotInfoCommand

    @Inject
    private lateinit var plugin: ReferralSpigot

    override fun loadCommands() {
        val subCommands: MutableMap<List<String>, CommandExecutor> = mutableMapOf()
        subCommands[BY_ALIAS] = byCommand
        subCommands[INFO_ALIAS] = infoCommand
        subCommands[HELP_ALIAS] = commandService.generateHelpCommand(this)
        subCommands[VERSION_ALIAS] = commandService.generateVersionCommand(HELP_USAGE)

        val root = plugin.getCommand(name)!!

        root.setExecutor(
            commandService.generateRoutingCommand(
                commandService.generateRootCommand(HELP_USAGE), subCommands, false
            )
        )
    }
}
