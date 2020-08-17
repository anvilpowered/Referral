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

package org.anvilpowered.referral.api.registry;

import com.google.common.collect.ImmutableList;
import org.anvilpowered.anvil.api.registry.Key;
import org.anvilpowered.anvil.api.registry.Keys;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReferralKeys {

    //permissions
    public static final Key<String> FROM_PERMISSION = new Key<String>("FROM_PERMISSION",
        "referral.user.from") {
    };
    public static final Key<String> INFO_PERMISSION = new Key<String>(
        "INFO_PERMISSION", "referral.admin.info") {
    };

    //rewards
    public static final Key<Integer> REWARD_ECO = new Key<Integer>(
        "REWARD_ECO", 100) {
    };
    public static final Key<List<String>> REWARD_ITEMS = new Key<List<String>>(
        "REWARD_ITEMS", ImmutableList.of("minecraft:dirt", "minecraft:stone")) {
    };
    public static final Key<String> REWARD_KIT = new Key<String>(
        "REWARD_KIT", "") {
    };
    public static final Key<List<String>> REWARD_PERMISSIONS = new Key<List<String>>(
        "REWARD_PERMISSIONS", ImmutableList.of()) {
    };
    public static final Key<List<String>> REWARD_COMMANDS = new Key<List<String>>(
        "REWARD_COMMANDS", ImmutableList.of()) {
    };

    //toggles for rewards
    public static final Key<Boolean> ECO_ENABLED = new Key<Boolean>(
        "ECO_ENABLED", true) {
    };
    public static final Key<Boolean> ITEMS_ENABLED = new Key<Boolean>(
        "ITEMS_ENABLED", true) {
    };
    public static final Key<Boolean> KIT_ENABLED = new Key<Boolean>(
        "KIT_ENABLED", false) {
    };
    public static final Key<Boolean> COMMANDS_ENABLED = new Key<Boolean>(
        "COMMANDS_ENABLED", false) {
    };

    //toggle for tiered mode
    public static final Key<Boolean> TIERED_MODE_ENABLED = new Key<Boolean>(
        "TIERED_MODE_ENABLED", false) {
    };

    //tiers
    static List<Tier> tiers = new LinkedList<>();
    public static final Key<List<Tier>> TIERS = new Key<List<Tier>>(
        "TIERS", tiers) {
    };

    //root nodes for each section
    public static final Key<String> MODULES_ROOT = new Key<String>(
        "MODULES_ROOT", null) {
    };
    public static final Key<String> TIERS_ROOT = new Key<String>(
        "TIERS_ROOT", null) {
    };
    public static final Key<String> REWARDS_ROOT = new Key<String>(
        "REWARDS_ROOT", null) {
    };

    static {
        Keys.startRegistration("referral")
            .register(FROM_PERMISSION)
            .register(INFO_PERMISSION)
            .register(REWARD_ECO)
            .register(REWARD_ITEMS)
            .register(REWARD_KIT)
            .register(REWARD_PERMISSIONS)
            .register(REWARD_COMMANDS)
            .register(ECO_ENABLED)
            .register(ITEMS_ENABLED)
            .register(KIT_ENABLED)
            .register(COMMANDS_ENABLED)
            .register(TIERED_MODE_ENABLED)
            .register(TIERS)
            .register(MODULES_ROOT)
            .register(TIERS_ROOT)
            .register(REWARDS_ROOT);
    }

    //default tiers
    static {
        Map<String, Integer> ironItems = new HashMap<>();
        ironItems.put("minecraft:iron_ingot", 1);
        ironItems.put("minecraft:stone", 10);
        Tier iron = new Tier();
        iron.tierName = "Iron";
        iron.referralRequirement = 1;
        iron.economy = 10;
        iron.items = ImmutableList.of(ironItems);
        iron.kit = "";
        iron.permissions = ImmutableList.of();
        iron.commands = ImmutableList.of("say %player% has been promoted to the Iron referral tier!");

        Map<String, Integer> goldItems = new HashMap<>();
        goldItems.put("minecraft:gold_ingot", 16);
        goldItems.put("minecraft:iron_ingot", 16);
        Tier gold = new Tier();
        gold.tierName = "Gold";
        gold.referralRequirement = 10;
        gold.economy = 100;
        gold.items = ImmutableList.of(goldItems);
        gold.kit = "";
        gold.permissions = ImmutableList.of();
        gold.commands = ImmutableList.of("say %player% has been promoted to the Gold referral tier!");

        tiers.add(iron);
        tiers.add(gold);
    }
}
