package org.anvilpowered.referral.api.service;

import org.anvilpowered.referral.api.registry.Tier;

public interface RewardService<TPlayer> {

    void giveTierRewardsToPlayer(TPlayer player, Tier tier);

    void giveRewardsToReferrer(TPlayer referrer);

    void giveRewardsToReferee(TPlayer referee);
}
