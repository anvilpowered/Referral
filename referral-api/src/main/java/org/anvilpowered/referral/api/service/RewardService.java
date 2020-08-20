package org.anvilpowered.referral.api.service;

import org.anvilpowered.referral.api.registry.Tier;

public interface RewardService<TUser> {

    void giveTierRewardsToUser(TUser user, Tier tier);

    void giveRewardsToReferrer(TUser referrer);

    void giveRewardsToReferee(TUser referee);
}
