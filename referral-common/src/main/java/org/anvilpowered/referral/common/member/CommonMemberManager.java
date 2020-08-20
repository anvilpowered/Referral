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
import org.anvilpowered.referral.api.registry.Tier;
import org.anvilpowered.referral.api.service.RewardService;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private RewardService<TUser> rewardService;

    @Inject
    private Logger logger;

    @Override
    public CompletableFuture<TString> info(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            final String userName = userService.getUserName(userUUID).join().orElse("null");
            if (!optionalMember.isPresent()) {
                return textService.builder()
                    .append(pluginInfo.getPrefix())
                    .red().append("Could not find user ", userName)
                    .build();
            }
            final Member<?> member = optionalMember.get();
            TextService.Builder<TString, TCommandSource> builder = textService.builder()
                .append(
                    textService.builder()
                        .dark_gray().append("========= ")
                        .gold().append(userName)
                        .dark_gray().append(" =========")
                )
                .gray().append("\n\nReferred by: ");
            if (member.getReferrerUserUUID() == null) {
                builder.red().append("Not referred\n\n");
            } else {
                builder.aqua().append(
                    userService.getUserName(member.getReferrerUserUUID()).join().orElse("null")
                );
            }
            builder.gray().append("\nReferrals: ");
            final List<UUID> referrals = member.getReferredUserUUIDs();
            if (referrals.size() == 0) {
                builder.red().append("No referrals");
            } else {
                for (UUID uuid : referrals) {
                    builder.aqua().append(userService.getUserName(uuid).join().orElse("null"));
                }
                if (registry.getOrDefault(ReferralKeys.TIERED_MODE_ENABLED)) {
                    Optional<Tier> currentTier = getCurrentTier(member);
                    builder.gray().append("\n\nTier: ");
                    if (currentTier.isPresent()) {
                        builder.aqua().append(currentTier.get().name);
                    } else {
                        builder.red().append("null");
                    }
                }
            }
            return builder.append("\n\n", textService.builder()
                .dark_gray().append("========= ")
                .gold().append(pluginInfo.getPrefix())
                .dark_gray().append("========="))
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
        return getPrimaryComponent().getOneOrGenerateIfNotReferredForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent() ||
                !getPrimaryComponent().refer(userUUID, targetUserUUID).join()) {
                return textService.builder()
                    .append(pluginInfo.getPrefix())
                    .red().append("You have already been referred!")
                    .build();
            }
            optionalMember.get().getReferredUserUUIDs().add(targetUserUUID);
            userService.getPlayer(targetUserUUID).ifPresent(p ->
                textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("You have successfully referred ")
                    .gold().append(userService.getUserName(userUUID).join().orElse("null"))
                    .sendTo((TCommandSource) p));
            final Optional<TUser> referrer = userService.get(targetUserUUID);
            final Optional<TUser> referee = userService.get(userUUID);
            if (registry.getOrDefault(ReferralKeys.TIERED_MODE_ENABLED)) {
                final Optional<Tier> nextTier = getNextTier(optionalMember.get());
                if (referrer.isPresent() && nextTier.isPresent()) {
                    rewardService.giveTierRewardsToUser( referrer.get(), nextTier.get());
                }
            } else {
                if (referrer.isPresent()) {
                    rewardService.giveRewardsToReferrer( referrer.get());
                } else {
                    logger.error("{}'s referrer is not online, rewards will not be processed",
                        userService.getUserName(referee.get()));
                }
            }
            referee.ifPresent(user -> rewardService.giveRewardsToReferee( user));
            return textService.builder()
                .append(pluginInfo.getPrefix())
                .green().append("You have successfully been referred by ")
                .gold().append(userService.getUserName(targetUserUUID).join().orElse("null"))
                .build();
        });
    }

    private Optional<Tier> getNextTier(Member<?> member) {
        Optional<Tier> currentTier = getCurrentTier(member);
        if (!currentTier.isPresent()) {
            if (getLowestTier().referralRequirement <= member.getReferredUserUUIDs().size() + 1) {
                return Optional.of(getLowestTier());
            }
            return Optional.empty();
        } else {
            for (Tier tier : registry.getOrDefault(ReferralKeys.TIERS)) {
                if (member.getReferredUserUUIDs().size() + 1 == tier.referralRequirement) {
                    return Optional.of(tier);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Tier> getCurrentTier(Member<?> member) {
        int totalReferrals = member.getReferredUserUUIDs().size();
        int currentHighest = -1;
        Tier result = null;
        for (Tier tier : registry.getOrDefault(ReferralKeys.TIERS)) {
            if (tier.referralRequirement > currentHighest
                && tier.referralRequirement <= totalReferrals) {
                result = tier;
                currentHighest = tier.referralRequirement;
            }
        }
        return Optional.ofNullable(result);
    }

    private Tier getLowestTier() {
        return Collections.min(registry.getOrDefault(ReferralKeys.TIERS),
            Comparator.comparing(tier -> tier.referralRequirement));
    }
}
