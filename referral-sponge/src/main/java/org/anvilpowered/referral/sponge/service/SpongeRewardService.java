package org.anvilpowered.referral.sponge.service;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.referral.api.registry.ReferralKeys;
import org.anvilpowered.referral.api.registry.Tier;
import org.anvilpowered.referral.api.service.RewardService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class SpongeRewardService implements RewardService<User> {

    @Inject
    private Registry registry;

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    private Logger logger;

    @Override
    public void giveTierRewardsToUser(User user, Tier tier) {
        if (registry.getOrDefault(ReferralKeys.ECO_ENABLED)) {
            final Optional<EconomyService> economyService = Sponge.getServiceManager().provide(EconomyService.class);
            if (economyService.isPresent()) {
                final Optional<UniqueAccount> account =
                    economyService.get().getOrCreateAccount(user.getUniqueId());
                if (account.isPresent()) {
                    account.get().deposit(
                        economyService.get().getDefaultCurrency(),
                        BigDecimal.valueOf(tier.economy),
                        Cause.of(
                            EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(),
                            pluginContainer
                        ));
                } else {
                    logger.error("Could not find an account for {}", user.getName());
                }
            } else {
                logger.error("There is no economy service present! Economy rewards will not be given");
            }
        }
        if (registry.getOrDefault(ReferralKeys.ITEMS_ENABLED)) {
            for (Map<String, Integer> item : tier.items) {
                item.forEach((itemName, amount) -> {
                    String command = "give %player% " + itemName + " " + amount;
                    command = command.replace("%player%", user.getName());
                    Sponge.getCommandManager().process(
                        Sponge.getServer().getConsole().getCommandSource().get(),
                        command
                    );
                });
            }
        }
        if (registry.getOrDefault(ReferralKeys.KIT_ENABLED)) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                "kit give " + user.getName() + " " + tier.kit);
        }
        if (registry.getOrDefault(ReferralKeys.PERMISSIONS_ENABLED)) {
            for (String permission : tier.permissions) {
                user.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, permission, Tristate.TRUE);
            }
        }
        if (registry.getOrDefault(ReferralKeys.COMMANDS_ENABLED)) {
            for (String command : tier.commands) {
                Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                    command.replaceAll("%player%", user.getName()));
            }
        }
        if (registry.getOrDefault(ReferralKeys.XP_ENABLED)) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                "xp " + tier.xp + " " + user.getName());
        }
    }

    @Override
    public void giveRewardsToReferrer(User referrer) {
        if (registry.getOrDefault(ReferralKeys.ECO_ENABLED)) {
            Optional<EconomyService> economyService = Sponge.getServiceManager().provide(EconomyService.class);
            economyService.ifPresent(eco ->
                eco.getOrCreateAccount(referrer.getUniqueId()).get().deposit(
                    eco.getDefaultCurrency(),
                    BigDecimal.valueOf(registry.getOrDefault(ReferralKeys.REFERRER_ECO)),
                    Cause.of(
                        EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(),
                        pluginContainer
                    )
                )
            );
        }
        if (registry.getOrDefault(ReferralKeys.ITEMS_ENABLED)) {
            for (Map<String, Integer> item : registry.getOrDefault(ReferralKeys.REFERRER_ITEMS)) {
                item.forEach((itemName, amount) -> {
                    String command = "give %player% " + itemName + " " + amount;
                    command = command.replaceAll("%player%", referrer.getName());
                    Sponge.getCommandManager().process(
                        Sponge.getServer().getConsole().getCommandSource().get(),
                        command
                    );
                });
            }
        }
        if (registry.getOrDefault(ReferralKeys.KIT_ENABLED)) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                "kit give " + referrer.getName() + " " + registry.getOrDefault(ReferralKeys.REFERRER_KIT));
        }
        if (registry.getOrDefault(ReferralKeys.PERMISSIONS_ENABLED)) {
            for (String permission : registry.getOrDefault(ReferralKeys.REFERRER_PERMISSIONS)) {
                referrer.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, permission, Tristate.TRUE);
            }
        }
        if (registry.getOrDefault(ReferralKeys.COMMANDS_ENABLED)) {
            for (String command : registry.getOrDefault(ReferralKeys.REFERRER_COMMANDS)) {
                Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                    command.replaceAll("%player%", referrer.getName()));
            }
        }
        if (registry.getOrDefault(ReferralKeys.XP_ENABLED)) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                "xp " + registry.getOrDefault(ReferralKeys.REFERRER_XP) + " " + referrer.getName());

        }
    }

    @Override
    public void giveRewardsToReferee(User referee) {
        if (registry.getOrDefault(ReferralKeys.ECO_ENABLED)) {
            Optional<EconomyService> economyService = Sponge.getServiceManager().provide(EconomyService.class);
            economyService.ifPresent(eco ->
                eco.getOrCreateAccount(referee.getUniqueId()).get().deposit(
                    eco.getDefaultCurrency(),
                    BigDecimal.valueOf(registry.getOrDefault(ReferralKeys.REFEREE_ECO)),
                    Cause.of(
                        EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(),
                        pluginContainer
                    )
                )
            );
        }
        if (registry.getOrDefault(ReferralKeys.ITEMS_ENABLED)) {
            for (Map<String, Integer> item : registry.getOrDefault(ReferralKeys.REFEREE_ITEMS)) {
                item.forEach((itemName, amount) -> {
                    String command = "give %player% " + itemName + " " + amount;
                    command = command.replaceAll("%player%", referee.getName());
                    Sponge.getCommandManager().process(
                        Sponge.getServer().getConsole().getCommandSource().get(),
                        command
                    );
                });
            }
        }
        if (registry.getOrDefault(ReferralKeys.KIT_ENABLED)) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                "kit give " + referee.getName() + " " + registry.getOrDefault(ReferralKeys.REFEREE_KIT));
        }
        if (registry.getOrDefault(ReferralKeys.PERMISSIONS_ENABLED)) {
            for (String permission : registry.getOrDefault(ReferralKeys.REFEREE_PERMISSIONS)) {
                referee.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, permission, Tristate.TRUE);
            }
        }
        if (registry.getOrDefault(ReferralKeys.COMMANDS_ENABLED)) {
            for (String command : registry.getOrDefault(ReferralKeys.REFEREE_COMMANDS)) {
                command = command.replaceAll("%player%", referee.getName());
                Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(), command);
            }
        }
        if (registry.getOrDefault(ReferralKeys.XP_ENABLED)) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole().getCommandSource().get(),
                "xp " + registry.getOrDefault(ReferralKeys.REFEREE_XP) + " " + referee.getName());
        }
    }
}
