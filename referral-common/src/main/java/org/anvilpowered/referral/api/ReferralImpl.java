package org.anvilpowered.referral.api;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.referral.common.plugin.Referral;

@SuppressWarnings("UnstableApiUsage")
public class ReferralImpl extends Referral {

    protected ReferralImpl(Injector injector, Module module) {
        super(injector, module);
    }

    @Override
    protected void applyToBuilder(Environment.Builder builder) {
        builder.withRootCommand();
    }
}
