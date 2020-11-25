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

package org.anvilpowered.referral.velocity.command

import com.google.inject.Inject
import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.referral.common.ReferralPluginInfo
import org.anvilpowered.referral.common.command.CommonReferralCommandNode

class VelocityReferralCommandNode @Inject constructor(
    registry: Registry
) : CommonReferralCommandNode<Command, CommandSource>(registry) {

    @Inject
    private lateinit var proxyServer: ProxyServer

    @Inject
    private lateinit var byCommand: VelocityByCommand

    @Inject
    private lateinit var infoCommand: VelocityInfoCommand

    override fun loadCommands() {
        val subCommands: MutableMap<List<String>, Command> = mutableMapOf()
        subCommands[BY_ALIAS] = byCommand
        subCommands[INFO_ALIAS] = infoCommand
        subCommands[HELP_ALIAS] = commandService.generateHelpCommand(this)
        subCommands[VERSION_ALIAS] = commandService.generateVersionCommand(HELP_COMMAND)
        proxyServer.commandManager.register(
            proxyServer.commandManager.metaBuilder(ReferralPluginInfo.id).aliases("ref").build(),
            commandService.generateRoutingCommand(
                commandService.generateRootCommand(HELP_COMMAND), subCommands, false
            )
        )
    }
}
