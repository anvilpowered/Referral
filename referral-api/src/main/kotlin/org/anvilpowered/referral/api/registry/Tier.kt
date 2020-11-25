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

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

@ConfigSerializable
class Tier {

    val alphanumeric = "^([a-zA-Z0-9_]|(&[a-f0-9]))*$".toRegex()

    @Setting(
        comment = """
The amount of referrals required to reach this tier.
""",
    )
    var referralRequirement: Int = 0

    @Setting(
        comment = """
The commands to run when a player reaches this tier.
""",
    )
    var commands: List<String> = listOf()

    @Setting(
        comment = """

        """
    )
    var description: String = "none"

    @Setting(
        comment = """
The name of the tier. You may use alphanumeric characters and color codes.
The formally accepted regex is: ^([a-zA-Z0-9_]|(&[a-f0-9]))*$
""",
    )
    var name: String = "none"

    constructor()

    constructor(name: String, referralRequirement: Int, commands: List<String>) {
        this.name = name
        this.referralRequirement = referralRequirement
        this.commands = commands
    }
}
