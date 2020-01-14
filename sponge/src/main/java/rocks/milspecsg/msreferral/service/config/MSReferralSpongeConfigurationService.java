package rocks.milspecsg.msreferral.service.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.DefaultConfig;
import rocks.milspecsg.msreferral.service.common.data.config.MSReferralConfigurationService;

@Singleton
public class MSReferralSpongeConfigurationService extends MSReferralConfigurationService {

    @Inject
    public MSReferralSpongeConfigurationService(@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }
}
