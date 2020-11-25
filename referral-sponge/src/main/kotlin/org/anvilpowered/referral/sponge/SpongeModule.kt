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

package org.anvilpowered.referral.sponge

import com.google.inject.TypeLiteral
import org.anvilpowered.anvil.api.command.CommandNode
import org.anvilpowered.referral.common.CommonModule
import org.anvilpowered.referral.common.registry.CommonConfigurationService
import org.anvilpowered.referral.sponge.command.SpongeReferralCommandNode
import org.anvilpowered.referral.sponge.registry.SpongeConfigurationService
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.text.Text

class SpongeModule : CommonModule<User, Player, Text, CommandSource>() {

    override fun configure() {
        super.configure()
        bind(CommonConfigurationService::class.java).to(SpongeConfigurationService::class.java)
        bind(object : TypeLiteral<CommandNode<CommandSource>>() {}).to(SpongeReferralCommandNode::class.java)
    }
}
