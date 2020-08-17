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
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.referral.api.member.MemberManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Identifiable;

import java.util.Optional;

public class SpongeReferralInfoCommand implements CommandExecutor {

    @Inject
    private MemberManager<Text> memberManager;

    @Inject
    private PluginInfo<Text> pluginInfo;

    @Inject
    private TextService<Text, CommandSource> textService;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) {
        Optional<User> user = context.getOne(Text.of("user"));
        if (user.isPresent()) {
            memberManager.info(user.get().getUniqueId())
                .thenAcceptAsync(source::sendMessage)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
        } else if (source instanceof Identifiable) {
            memberManager.info(((Identifiable) source).getUniqueId())
                .thenAcceptAsync(source::sendMessage)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
        } else {
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Provide a user or run as player!")
                .sendTo(source);
            return CommandResult.empty();
        }
        return CommandResult.success();
    }
}
