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

package org.anvilpowered.referral.common.registry

import com.google.inject.Inject
import com.google.inject.Singleton
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.anvilpowered.anvil.api.registry.Keys
import org.anvilpowered.anvil.base.registry.BaseConfigurationService
import org.anvilpowered.referral.api.registry.ReferralKeys
import org.anvilpowered.referral.api.registry.Tier
import org.anvilpowered.referral.common.ReferralPluginInfo

@Singleton
open class CommonConfigurationService @Inject constructor(
    configLoader: ConfigurationLoader<CommentedConfigurationNode>
) : BaseConfigurationService(configLoader) {

    init {
        withMongoDB()
        setDefault(Keys.DATA_DIRECTORY, ReferralPluginInfo.id)
        setDefault(Keys.MONGODB_DBNAME, ReferralPluginInfo.id)
        setDefault(ReferralKeys.EACH_REFERRAL_COMMANDS, listOf(
            "give %player% redstone 5",
        ))
        setDefault(ReferralKeys.EACH_TIER_COMMANDS, listOf(
            "say %player% has reached tier %tier% after referring %lastref% (%refcount% total referrals)",
        ))
        val defaultTiers: MutableList<Tier> = mutableListOf()
        defaultTiers.add(Tier("wood", 2, listOf("give %player% oak_log 1",)))
        defaultTiers.add(Tier("stone", 5, listOf("give %player% stone 5",)))
        defaultTiers.add(Tier("iron", 10, listOf("give %player% iron 10")))
        setDefault(ReferralKeys.TIERS, defaultTiers)
        setName(ReferralKeys.TIER_ROOT, "rewards")
        setName(ReferralKeys.EACH_REFERRAL_COMMANDS, "rewards.eachReferralCommands")
        setName(ReferralKeys.EACH_TIER_COMMANDS, "rewards.eachTierCommands")
        setName(ReferralKeys.TIERS, "rewards.tiers")
        setDescription(ReferralKeys.TIER_ROOT, """
|------------------------------------------------------------|
|                           Rewards                          |
|------------------------------------------------------------|

The reward system is a way of rewarding players for repeated referrals, with the idea
being that every new tier provides better rewards than the last.

Available placeholders (for all commands defined in this file):
 - %player% : The player's username
 - %tier% : The player's current tier
 - %lastref% : The last user referred by the player
 - %refcount% : The player's referral count
""")
        setDescription(ReferralKeys.EACH_REFERRAL_COMMANDS, "\nThe commands to run after each referral")
        setDescription(ReferralKeys.EACH_TIER_COMMANDS, "\nThe commands to run after a new tier is reached")
        setDescription(ReferralKeys.TIERS, "\nThe tiers")
    }
}
