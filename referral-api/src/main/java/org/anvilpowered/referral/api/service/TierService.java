package org.anvilpowered.referral.api.service;

import org.anvilpowered.referral.api.registry.Tier;

import java.util.UUID;

public interface TierService {

    Tier getCurrentTierForUser(UUID userUUID);

    Tier getNextTierForUser(UUID userUUID);
}
