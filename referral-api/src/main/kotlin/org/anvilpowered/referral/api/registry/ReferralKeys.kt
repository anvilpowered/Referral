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

package org.anvilpowered.referral.api.registry

import com.google.common.reflect.TypeToken
import org.anvilpowered.anvil.api.registry.Key
import org.anvilpowered.anvil.api.registry.Keys
import org.anvilpowered.anvil.api.registry.TypeTokens

@Suppress("UnstableApiUsage")
object ReferralKeys {

    val LIST_STRING = object : TypeToken<List<String>>() {}
    val LIST_TIER = object : TypeToken<List<Tier>>() {}
    val UNIT = TypeToken.of(Unit::class.java)

    val EACH_REFERRAL_COMMANDS: Key<List<String>> =
        Key.builder(LIST_STRING)
            .name("EACH_REFERRAL_COMMANDS")
            .fallback(listOf())
            .build()
    val EACH_TIER_COMMANDS: Key<List<String>> =
        Key.builder(LIST_STRING)
            .name("EACH_TIER_COMMANDS")
            .fallback(listOf())
            .build()
    val TIERS: Key<List<Tier>> =
        Key.builder(LIST_TIER)
            .name("TIERS")
            .fallback(listOf())
            .build()

    val TIER_ROOT: Key<Unit> =
        Key.builder(UNIT)
            .name("TIER_ROOT")
            .build()

    val BY_PERMISSION: Key<String> =
        Key.builder(TypeTokens.STRING)
            .name("BY_PERMISSION")
            .fallback("referral.user.by")
            .build()
    val INFO_PERMISSION: Key<String> =
        Key.builder(TypeTokens.STRING)
            .name("INFO_PERMISSION")
            .fallback("referral.admin.info")
            .build()

    init {
        Keys.startRegistration("referral")
            .register(EACH_REFERRAL_COMMANDS)
            .register(EACH_TIER_COMMANDS)
            .register(TIERS)
            .register(BY_PERMISSION)
            .register(INFO_PERMISSION)
    }
}
