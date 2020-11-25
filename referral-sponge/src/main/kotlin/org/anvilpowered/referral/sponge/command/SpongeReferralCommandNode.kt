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

package org.anvilpowered.referral.sponge.command

import com.google.inject.Inject
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.referral.common.ReferralPluginInfo
import org.anvilpowered.referral.common.command.CommonReferralCommandNode
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandCallable
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.text.Text

class SpongeReferralCommandNode @Inject constructor(
    registry: Registry
) : CommonReferralCommandNode<CommandExecutor, CommandSource>(registry) {

    @Inject
    private lateinit var byCommand: SpongeByCommand

    @Inject
    private lateinit var infoCommand: SpongeInfoCommand

    override fun loadCommands() {
        val subCommands: MutableMap<List<String>, CommandCallable> = mutableMapOf()
        subCommands[BY_ALIAS] = byCommand
        subCommands[INFO_ALIAS] = infoCommand
        subCommands[HELP_ALIAS] = CommandSpec.builder()
            .description(Text.of(HELP_USAGE))
            .executor(commandService.generateHelpCommand(this))
            .build()
        subCommands[VERSION_ALIAS] = CommandSpec.builder()
            .description(Text.of(VERSION_DESCRIPTION))
            .executor(commandService.generateVersionCommand(HELP_USAGE))
            .build()
        val command = CommandSpec.builder()
            .description(Text.of(ROOT_DESCRIPTION))
            .executor(commandService.generateRootCommand(HELP_USAGE))
            .children(subCommands)
            .build()
        Sponge.getCommandManager()
            .register(environment.plugin, command, ReferralPluginInfo.id, "ref")
    }
}
