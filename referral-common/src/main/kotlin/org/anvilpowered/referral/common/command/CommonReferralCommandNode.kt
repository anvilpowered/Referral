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

package org.anvilpowered.referral.common.command

import com.google.inject.Inject
import org.anvilpowered.anvil.api.Environment
import org.anvilpowered.anvil.api.command.CommandNode
import org.anvilpowered.anvil.api.command.CommandService
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.referral.common.ReferralPluginInfo
import java.util.function.Function
import java.util.function.Predicate

abstract class CommonReferralCommandNode<TCommandExecutor, TCommandSource>(
    registry: Registry
) : CommandNode<TCommandSource> {

    companion object {
        val BY_ALIAS = listOf("by", "from")
        val INFO_ALIAS = listOf("info", "check")
        val HELP_ALIAS = listOf("help")
        val VERSION_ALIAS = listOf("version")

        const val BY_DESCRIPTION = "Referral by command"
        const val INFO_DESCRIPTION = "View referral info for a user"
        const val HELP_DESCRIPTION = "Shows this help page."
        const val VERSION_DESCRIPTION = "Shows plugin version."
        const val ROOT_DESCRIPTION = "${ReferralPluginInfo.name} root command"

        const val BY_USAGE = "<user>"
        const val INFO_USAGE = "[<user>]"
        const val HELP_USAGE = "/referral help"
    }

    private var alreadyLoaded = false
    private val descriptions: MutableMap<List<String>, Function<TCommandSource, String>> = mutableMapOf()
    private val permissions: MutableMap<List<String>, Predicate<TCommandSource>> = mutableMapOf()
    private val usages: MutableMap<List<String>, Function<TCommandSource, String>> = mutableMapOf()

    @Inject
    protected lateinit var commandService: CommandService<TCommandExecutor, TCommandSource>

    @Inject
    protected lateinit var environment: Environment

    init {
        registry.whenLoaded {
            if (alreadyLoaded) return@whenLoaded
            loadCommands()
            alreadyLoaded = true
        }.register()
        descriptions.put(BY_ALIAS) { BY_DESCRIPTION }
        descriptions.put(INFO_ALIAS) { INFO_DESCRIPTION }
        descriptions.put(HELP_ALIAS) { HELP_DESCRIPTION }
        descriptions.put(VERSION_ALIAS) { VERSION_DESCRIPTION }
        usages.put(BY_ALIAS) { BY_USAGE }
        usages.put(INFO_ALIAS) { INFO_USAGE }
    }

    protected abstract fun loadCommands()
    override fun getName(): String = ReferralPluginInfo.id
    override fun getDescriptions(): Map<List<String>, Function<TCommandSource, String>> = descriptions
    override fun getPermissions(): Map<List<String>, Predicate<TCommandSource>> = permissions
    override fun getUsages(): Map<List<String>, Function<TCommandSource, String>> = usages
}
