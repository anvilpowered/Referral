package rocks.milspecsg.msreferral.commands;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msreferral.api.member.MemberManager;
import rocks.milspecsg.msrepository.api.util.PluginInfo;

import java.util.Optional;

public class ReferredByCommand implements CommandExecutor {

    @Inject
    PluginInfo<Text> pluginInfo;

    @Inject
    MemberManager<Text> memberManager;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) {

        Optional<Player> targetPlayer = context.getOne(Text.of("user"));

        if (source instanceof Player) {
            Player player = (Player) source;
            targetPlayer.ifPresent(value -> memberManager.referredBy(player.getUniqueId(), value.getUniqueId()).exceptionally(e -> {
                e.printStackTrace();
                return Text.of("Hanging");
            }).thenAcceptAsync(source::sendMessage));
        } else {
            source.sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.RED, "This command can only be executed as a player!"));
        }
        return CommandResult.success();
    }
}
