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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReferralKeys {

    //permissions
    public static final Key<String> BY_PERMISSION = new Key<String>("BY_PERMISSION",
        "referral.user.by") {
    };
    public static final Key<String> INFO_PERMISSION = new Key<String>(
        "INFO_PERMISSION", "referral.admin.info") {
    };

    //rewards
    public static final Key<Integer> REFERRER_ECO = new Key<Integer>(
        "REFERRER_ECO", 100) {
    };
    public static final Key<List<Map<String, Integer>>> REFERRER_ITEMS = new Key<List<Map<String, Integer>>>(
        "REFERRER_ITEMS", ImmutableList.of(Collections.singletonMap("minecraft:dirt", 1))) {
    };
    public static final Key<String> REFERRER_KIT = new Key<String>(
        "REFERRER_KIT", "") {
    };
    public static final Key<List<String>> REFERRER_PERMISSIONS = new Key<List<String>>(
        "REFERRER_PERMISSIONS", ImmutableList.of()) {
    };
    public static final Key<List<String>> REFERRER_COMMANDS = new Key<List<String>>(
        "REFERRER_COMMANDS", ImmutableList.of()) {
    };
    public static final Key<Integer> REFERRER_XP = new Key<Integer>(
        "REFERRER_XP", 10) {
    };
    public static final Key<Integer> REFEREE_ECO = new Key<Integer>(
        "REFEREE_ECO", 100) {
    };
    public static final Key<List<Map<String, Integer>>> REFEREE_ITEMS = new Key<List<Map<String, Integer>>>(
        "REFEREE_ITEMS", ImmutableList.of(Collections.singletonMap("minecraft:dirt", 1))) {
    };
    public static final Key<String> REFEREE_KIT = new Key<String>(
        "REFEREE_KIT", "") {
    };
    public static final Key<List<String>> REFEREE_PERMISSIONS = new Key<List<String>>(
        "REFEREE_PERMISSIONS", ImmutableList.of()) {
    };
    public static final Key<List<String>> REFEREE_COMMANDS = new Key<List<String>>(
        "REFEREE_COMMANDS", ImmutableList.of()) {
    };
    public static final Key<Integer> REFEREE_XP = new Key<Integer>(
        "REFEREE_XP", 10) {
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
    public static final Key<Boolean> PERMISSIONS_ENABLED = new Key<Boolean>(
        "PERMISSIONS_ENABLED", false) {
    };
    public static final Key<Boolean> COMMANDS_ENABLED = new Key<Boolean>(
        "COMMANDS_ENABLED", true) {
    };
    public static final Key<Boolean> XP_ENABLED = new Key<Boolean>(
        "XP_ENABLED", true) {
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
            .register(BY_PERMISSION)
            .register(INFO_PERMISSION)
            .register(REFERRER_ECO)
            .register(REFERRER_ITEMS)
            .register(REFERRER_KIT)
            .register(REFERRER_PERMISSIONS)
            .register(REFERRER_COMMANDS)
            .register(REFERRER_XP)
            .register(REFEREE_ECO)
            .register(REFEREE_ITEMS)
            .register(REFEREE_KIT)
            .register(REFEREE_PERMISSIONS)
            .register(REFEREE_COMMANDS)
            .register(REFEREE_XP)
            .register(ECO_ENABLED)
            .register(ITEMS_ENABLED)
            .register(KIT_ENABLED)
            .register(PERMISSIONS_ENABLED)
            .register(COMMANDS_ENABLED)
            .register(XP_ENABLED)
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
        iron.xp = 10;

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
        gold.xp = 100;

        tiers.add(iron);
        tiers.add(gold);
    }
}
