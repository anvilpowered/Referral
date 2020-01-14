package rocks.milspecsg.msreferral.service.common.data.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msrepository.api.data.key.Keys;
import rocks.milspecsg.msrepository.common.data.config.CommonConfigurationService;

@Singleton
public class MSReferralConfigurationService extends CommonConfigurationService {

    @Inject
    public MSReferralConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
        defaultMap.put(Keys.MONGODB_DBNAME, "msreferral");
    }
}
