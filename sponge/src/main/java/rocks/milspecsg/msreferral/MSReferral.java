package rocks.milspecsg.msreferral;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msreferral.commands.ReferralCommandManager;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.sponge.module.ApiSpongeModule;

@Plugin(id = MSReferralPluginInfo.id,
    name = MSReferralPluginInfo.name,
    version = MSReferralPluginInfo.version,
    description = MSReferralPluginInfo.description,
    dependencies = @Dependency(id = "mscore"),
    url = MSReferralPluginInfo.url,
    authors = MSReferralPluginInfo.authors)
public class MSReferral {

    @Inject
    private Logger logger;

    @Inject
    private Injector spongeRootInjector;

    private static MSReferral plugin;
    private Injector injector;
    private PluginInfo<Text> pluginInfo = null;

    private boolean alreadyLoadedOnce = false;

    @Listener
    public void onServerInitialization(GameInitializationEvent event) {
        plugin = this;
        injector = spongeRootInjector.createChildInjector(new SpongeModule(), new ApiSpongeModule());
        pluginInfo = injector.getInstance(Key.get(new TypeLiteral<PluginInfo<Text>>() {
        }));
        logger.info(pluginInfo.getId() + "Loading...");
        initCommands();
        loadRegistry();
        logger.info(pluginInfo.getId() + "Loaded.");
    }

    @Listener
    public void reload(GameReloadEvent event) {
        loadRegistry();
        logger.info("Reloaded Successfully");
    }

    @Listener
    public void stop(GameStoppingEvent event) {
        logger.info(pluginInfo.getPrefix() + "Stopping");
    }

    private void loadRegistry() {
        injector.getInstance(Registry.class).load(this);
    }

    private void initCommands() {
        if(!alreadyLoadedOnce) {
            injector.getInstance(ReferralCommandManager.class).register(this);
            alreadyLoadedOnce = true;
        }
    }


}
