package rocks.milspecsg.msreferral;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msreferral.service.common.data.config.MSReferralConfigurationService;
import rocks.milspecsg.msreferral.service.config.MSReferralSpongeConfigurationService;

public class SpongeModule extends CommonModule<User, Player, Text, CommandSource> {

    @Override
    protected void configure() {
        super.configure();

        bind(MSReferralConfigurationService.class).to(MSReferralSpongeConfigurationService.class);
    }
}
