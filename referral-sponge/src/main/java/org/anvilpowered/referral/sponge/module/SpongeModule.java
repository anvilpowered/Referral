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

package org.anvilpowered.referral.sponge.module;

import com.google.inject.TypeLiteral;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.referral.api.service.RewardService;
import org.anvilpowered.referral.common.module.CommonModule;
import org.anvilpowered.referral.common.registry.CommonConfigurationService;
import org.anvilpowered.referral.sponge.commands.SpongeReferralCommandNode;
import org.anvilpowered.referral.sponge.registry.SpongeConfigurationService;
import org.anvilpowered.referral.sponge.service.SpongeRewardService;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class SpongeModule extends CommonModule<
    User,
    Player,
    Text,
    CommandSource> {

    @Override
    protected void configure() {
        super.configure();
        bind(CommonConfigurationService.class).to(SpongeConfigurationService.class);

        bind(new TypeLiteral<CommandNode<CommandSource>>() {
        }).to(SpongeReferralCommandNode.class);

        bind(new TypeLiteral<RewardService<Player>>() {
        }).to(SpongeRewardService.class);
    }
}
