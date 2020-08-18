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

package org.anvilpowered.referral.sponge.commands;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.referral.api.registry.ReferralKeys;
import org.anvilpowered.referral.common.command.CommonReferralCommandNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpongeReferralCommandNode
    extends CommonReferralCommandNode<CommandExecutor, CommandSource> {

    @Inject
    private SpongeReferralFromCommand referralFromCommand;

    @Inject
    private SpongeReferralInfoCommand referralInfoCommand;

    @Inject
    public SpongeReferralCommandNode(Registry registry) {
        super(registry);
    }

    @Override
    public void loadCommands() {

        Map<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(FROM_ALIAS, CommandSpec.builder()
            .description(Text.of(FROM_DESCRIPTION))
            .permission(registry.getOrDefault(ReferralKeys.FROM_PERMISSION))
            .arguments(GenericArguments.onlyOne(
                GenericArguments.user(Text.of("user")))
            )
            .executor(referralFromCommand)
            .build()
        );

        subCommands.put(INFO_ALIAS, CommandSpec.builder()
            .description(Text.of(INFO_DESCRIPTION))
            .permission(registry.getOrDefault(ReferralKeys.INFO_PERMISSION))
            .arguments(GenericArguments.optional(GenericArguments.onlyOne(
                GenericArguments.user(Text.of("user"))))
            )
            .executor(referralInfoCommand)
            .build()
        );

        subCommands.put(HELP_ALIAS, CommandSpec.builder()
            .description(Text.of(HELP_DESCRIPTION))
            .executor(commandService.generateHelpCommand(this))
            .build()
        );

        subCommands.put(VERSION_ALIAS, CommandSpec.builder()
            .description(Text.of(VERSION_DESCRIPTION))
            .executor(commandService.generateVersionCommand(HELP_COMMAND))
            .build()
        );

        CommandSpec root = CommandSpec.builder()
            .description(Text.of(ROOT_DESCRIPTION))
            .executor(commandService.generateRootCommand(HELP_COMMAND))
            .children(subCommands)
            .build();

        Sponge.getCommandManager()
            .register(environment.getPlugin(), root, "referral");
    }
}
