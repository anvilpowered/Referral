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

package plugin

import com.google.inject.Inject
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.referral.api.plugin.PluginMessages

class CommonPluginMessages<TString, TCommandSource> : PluginMessages<TString> {

    @Inject
    private lateinit var textService: TextService<TString, TCommandSource>

    private val noPermissionText: TString by lazy {
        textService.builder()
            .appendPrefix()
            .red().append("You do not have permission for this command!")
            .build()
    }

    override fun getNoPermission(): TString = noPermissionText

    private val selfReferText: TString by lazy {
        textService.builder()
            .appendPrefix()
            .red().append("You cannot refer yourself")
            .build()
    }

    override fun getSelfRefer(): TString = selfReferText

    private val alreadyReferredText: TString by lazy {
        textService.builder()
            .appendPrefix()
            .red().append("You have already been referred!")
            .build()
    }

    override fun getAlreadyReferred(): TString = alreadyReferredText

    override fun getFailedReferral(referrerUserName: String): TString {
        return textService.builder()
            .appendPrefix()
            .red().append("Failed to receive referral from ")
            .gold()
            .append(referrerUserName)
            .build()
    }

    override fun getReferralSuccess(referrerUserName: String): TString {
        return textService.builder()
            .appendPrefix()
            .green().append("You have successfully been referred by ")
            .gold().append(referrerUserName)
            .build()
    }

    override fun getReferredSuccess(userName: String): TString {
        return textService.builder()
            .appendPrefix()
            .green().append("You have successfully referred ")
            .gold().append(userName)
            .build()
    }

    override fun getPlayerNotFound(userName: String): TString {
        return textService.builder()
            .appendPrefix()
            .red().append("Could not find ")
            .gold().append(userName)
            .build()
    }
}
