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

package org.anvilpowered.referral.common

import com.google.inject.Inject
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.util.TextService

class ReferralPluginInfo<TString : Any, TCommandSource> : PluginInfo<TString> {
    companion object {
        const val id = "referral"
        const val name = "Referral"
        const val version = "\$modVersion"
        const val description = "Playtime tracker"
        const val url = "https://github.com/AnvilPowered/Referral"
        val authors = arrayOf("Cableguy20")
        const val organizationName = "AnvilPowered"
        const val buildDate = "\$buildDate"
    }

    lateinit var pluginPrefix: TString

    @Inject
    fun setPluginPrefix(textService: TextService<TString, TCommandSource>) {
        pluginPrefix = textService.builder().gold().append("[", name, "] ").build()
    }

    override fun getId(): String = Companion.id
    override fun getName(): String = Companion.name
    override fun getVersion(): String = Companion.version
    override fun getDescription(): String = Companion.description
    override fun getUrl(): String = Companion.url
    override fun getAuthors(): Array<String> = Companion.authors
    override fun getOrganizationName(): String = Companion.organizationName
    override fun getBuildDate(): String = Companion.buildDate
    override fun getPrefix(): TString = pluginPrefix
}
