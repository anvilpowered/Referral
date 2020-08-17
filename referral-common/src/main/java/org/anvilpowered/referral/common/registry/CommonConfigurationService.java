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

package org.anvilpowered.referral.common.registry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.anvilpowered.anvil.api.registry.Keys;
import org.anvilpowered.anvil.base.registry.BaseConfigurationService;
import org.anvilpowered.referral.api.registry.ReferralKeys;
import org.anvilpowered.referral.common.plugin.ReferralPluginInfo;

@Singleton
public class CommonConfigurationService extends BaseConfigurationService {

    @Inject
    public CommonConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
        setDefault(Keys.DATA_DIRECTORY, ReferralPluginInfo.id);
        setDefault(Keys.MONGODB_DBNAME, ReferralPluginInfo.id);
        //define nodes
        setName(ReferralKeys.MODULES_ROOT, "modules");
        setName(ReferralKeys.TIERS_ROOT, "tiers");
        setName(ReferralKeys.REWARDS_ROOT, "rewards");
        setName(ReferralKeys.REWARD_ECO, "rewards.eco");
        setName(ReferralKeys.REWARD_ITEMS, "rewards.items");
        setName(ReferralKeys.REWARD_KIT, "rewards.kit");
        setName(ReferralKeys.REWARD_PERMISSIONS, "rewards.permissions");
        setName(ReferralKeys.REWARD_COMMANDS, "rewards.commands");
        setName(ReferralKeys.ECO_ENABLED, "modules.eco");
        setName(ReferralKeys.ITEMS_ENABLED, "modules.items");
        setName(ReferralKeys.KIT_ENABLED, "modules.kit");
        setName(ReferralKeys.COMMANDS_ENABLED, "modules.commands");
        setName(ReferralKeys.TIERED_MODE_ENABLED, "modules.tiered");
        setName(ReferralKeys.TIERS, "tiers.tier");

        //comments
        setDescription(ReferralKeys.ECO_ENABLED, "\nToggle economy in rewards");
        setDescription(ReferralKeys.ITEMS_ENABLED, "\nToggle items in rewards");
        setDescription(ReferralKeys.KIT_ENABLED, "\nToggle kits in rewards");
        setDescription(ReferralKeys.COMMANDS_ENABLED, "\nToggle commands in rewards");
        setDescription(ReferralKeys.TIERED_MODE_ENABLED,
            "\nToggles tiered mode." +
                "\nWith this mode enabled, the \"referrer\" rewards will not be used."
        );

        //root headers
        setDescription(ReferralKeys.MODULES_ROOT,
            "|------------------------------------------------------------|\n" +
                "|                           Modules                          |\n" +
                "|------------------------------------------------------------| "
        );
        setDescription(ReferralKeys.TIERS_ROOT,
            "|------------------------------------------------------------|\n" +
                "|                           Tiers                            |\n" +
                "|------------------------------------------------------------| "
        );
        setDescription(ReferralKeys.REWARDS_ROOT,
            "|------------------------------------------------------------|\n" +
                "|                           Rewards                          |\n" +
                "|------------------------------------------------------------| "
        );
    }
}
