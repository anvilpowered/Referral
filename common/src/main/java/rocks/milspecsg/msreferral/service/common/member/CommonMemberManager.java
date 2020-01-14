package rocks.milspecsg.msreferral.service.common.member;

import com.google.inject.Inject;
import rocks.milspecsg.msreferral.api.member.MemberManager;
import rocks.milspecsg.msreferral.api.member.repository.MemberRepository;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.api.util.StringResult;
import rocks.milspecsg.msrepository.api.util.UserService;
import rocks.milspecsg.msrepository.common.manager.CommonManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMemberManager<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends CommonManager<MemberRepository<?, ?>>
    implements MemberManager<TString> {

    @Inject
    PluginInfo<TString> pluginInfo;

    @Inject
    StringResult<TString, TCommandSource> stringResult;

    @Inject
    UserService<TUser, TPlayer> userService;

    @Inject
    protected CommonMemberManager(Registry registry) {
        super(registry);
    }

    @Override
    public CompletableFuture<TString> info(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember ->
            optionalMember.isPresent()
                ? stringResult.builder()
                .append(stringResult.builder().dark_gray().append("========").gold().append(userService.getUserName(userUUID).orElse("null")).dark_gray().append("========="))
                .append("\n")
                .gray().append("Referrals: ").aqua().append("Names Here")
                .build()
                : stringResult.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find user ", userService.getUserName(userUUID).orElse(userUUID.toString()))
                .build());
    }

    @Override
    public CompletableFuture<TString> referredBy(UUID userUUID, UUID targetUserUUID) {
        return getPrimaryComponent().getOneOrGenerateIfNotReferredForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent() && getPrimaryComponent().parseAndAddToReferredList(optionalMember.get().getId(), targetUserUUID).join()) {
                userService.getPlayer(targetUserUUID).ifPresent(u ->
                    stringResult.builder()
                        .green().append("You have successfully referred ", userService.getUserName(userUUID).orElse("null"))
                        .sendTo(u));
                return stringResult.success("You have successfully been referred!");
            }
            return stringResult.fail("You have already been referred!");
        });
    }
}
