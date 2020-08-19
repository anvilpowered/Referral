package org.anvilpowered.referral.common.service;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.referral.api.member.MemberManager;
import org.anvilpowered.referral.api.registry.ReferralKeys;
import org.anvilpowered.referral.api.registry.Tier;
import org.anvilpowered.referral.api.service.TierService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class CommonTierService<TString> implements TierService {

    @Inject
    private Registry registry;

    @Inject
    private MemberManager<TString> memberManager;

    @Override
    public Tier getCurrentTierForUser(UUID userUUID) {
        return memberManager.getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                int totalReferrals = optionalMember.get().getReferredUserUUIDs().size();
                List<Tier> potentialTiers = new ArrayList<>();
                registry.getOrDefault(ReferralKeys.TIERS).forEach(tier -> {
                    if (tier.referralRequirement <= totalReferrals) {
                        potentialTiers.add(tier);
                    }
                });
                AtomicReference<Tier> tier = new AtomicReference<>(new Tier());
                potentialTiers.forEach(t -> {
                    if (tier.get().referralRequirement == 0) {
                        tier.set(t);
                    } else if (tier.get().referralRequirement < t.referralRequirement) {
                        tier.set(t);
                    }
                });
                return tier.get();
            }
            return null;
        }).join();
    }

    @Override
    public Tier getNextTierForUser(UUID userUUID) {
        return memberManager.getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                AtomicReference<Tier> nextTier = new AtomicReference<>(new Tier());
                Tier currentTier = getCurrentTierForUser(userUUID);
                registry.getOrDefault(ReferralKeys.TIERS).forEach(tier -> {
                    if (!tier.tierName.equals(nextTier.get().tierName)
                        && tier.referralRequirement > currentTier.referralRequirement) {
                        nextTier.set(tier);
                    }
                });
                return nextTier.get();
            }
            return null;
        }).join();
    }

    @Override
    public Tier getLowestTier() {
        AtomicReference<Tier> lowestTier = new AtomicReference<>();
        registry.getOrDefault(ReferralKeys.TIERS).forEach(t -> {
            if (lowestTier.get() == null) {
                lowestTier.set(t);
            }
            if (lowestTier.get().referralRequirement > t.referralRequirement) {
                lowestTier.set(t);
            }
        });
        return lowestTier.get();
    }
}
