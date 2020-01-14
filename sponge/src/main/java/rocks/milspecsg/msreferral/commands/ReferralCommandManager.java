package rocks.milspecsg.msreferral.commands;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class ReferralCommandManager implements CommandManager {

    @Inject
    private ReferredByCommand referredByCommand;

    @Override
    public void register(Object plugin) {
        CommandSpec mainCommand = CommandSpec.builder()
            .description(Text.of("referral command"))
            .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("user"))))
            .executor(referredByCommand)
            .build();

        Sponge.getCommandManager().register(plugin, mainCommand, "referredby");
    }
}
