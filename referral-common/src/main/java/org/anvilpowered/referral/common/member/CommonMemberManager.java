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

package org.anvilpowered.referral.common.member;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.anvil.base.datastore.BaseManager;
import org.anvilpowered.referral.api.member.MemberManager;
import org.anvilpowered.referral.api.member.MemberRepository;
import org.anvilpowered.referral.api.model.Member;
import org.anvilpowered.referral.api.registry.ReferralKeys;
import org.anvilpowered.referral.api.service.TierService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class CommonMemberManager<
    TUser,
    TPlayer,
    TString,
    TCommandSource>
    extends BaseManager<MemberRepository<?, ?>>
    implements MemberManager<TString> {

    @Inject
    protected PluginInfo<TString> pluginInfo;

    @Inject
    protected TextService<TString, TCommandSource> textService;

    @Inject
    protected UserService<TUser, TPlayer> userService;

    @Inject
    protected CommonMemberManager(Registry registry) {
        super(registry);
    }

    @Inject
    private TierService tierService;

    @Override
    public CompletableFuture<TString> info(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                Member<?> member = optionalMember.get();
                TextService.Builder<TString, TCommandSource> builder = textService.builder()
                    .append(
                        textService.builder()
                            .dark_gray().append("========= ")
                            .gold().append(userService.getUserName(userUUID))
                            .dark_gray().append(" =========")
                    )
                    .gray().append("\n\nReferred by: ");
                if (member.getReferrerUserUUID() == null) {
                    builder.red().append("Not referred\n\n");
                } else {
                    builder.aqua().append(userService.getUserName(member.getReferrerUserUUID()));
                }
                builder.gray().append("Referrals: ");
                List<UUID> referrals = member.getReferredUserUUIDs();
                if (referrals.size() == 0) {
                    builder.red().append("No referrals");
                } else {
                    builder.aqua().appendJoining(", ", referrals.stream().map(userService::getUserName));
                }
                return builder.append("\n\n", textService.builder()
                    .dark_gray().append("========= ")
                    .gold().append(pluginInfo.getPrefix())
                    .dark_gray().append("========="))
                    .build();
            }
            return textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find user ", userService.getUserName(userUUID))
                .build();
        });
    }

    @Override
    public CompletableFuture<TString> refer(UUID userUUID, UUID targetUserUUID) {
        if (userUUID.equals(targetUserUUID)) {
            return CompletableFuture.completedFuture(textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("You cannot refer yourself")
                .build());
        }
        return getPrimaryComponent().getOneOrGenerateIfNotReferredForUser(userUUID)
            .thenApplyAsync(optionalMember -> {
                if (optionalMember.isPresent() &&
                    getPrimaryComponent().refer(userUUID, targetUserUUID).join()) {
                    userService.getPlayer(targetUserUUID).ifPresent(p ->
                        textService.builder()
                            .append(pluginInfo.getPrefix())
                            .green().append("You have successfully referred ")
                            .gold().append(userService.getUserName(userUUID))
                            .sendTo((TCommandSource) p));
                    if (registry.getOrDefault(ReferralKeys.TIERED_MODE_ENABLED)) {
                        if(tierService.getCurrentTierForUser(userUUID).referralRequirement
                            >= tierService.getNextTierForUser(userUUID).referralRequirement) {
                            //TODO tier reward giving
                        }
                    }
                    return textService.builder()
                        .append(pluginInfo.getPrefix())
                        .green().append("You have successfully been referred by ")
                        .gold().append(userService.getUserName(targetUserUUID))
                        .build();
                }
                return textService.builder()
                    .append(pluginInfo.getPrefix())
                    .red().append("You have already been referred!")
                    .build();
            });
    }
}
