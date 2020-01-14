package rocks.milspecsg.msreferral;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import rocks.milspecsg.msreferral.api.member.MemberManager;
import rocks.milspecsg.msreferral.api.member.repository.MemberRepository;
import rocks.milspecsg.msreferral.service.common.data.config.MSReferralConfigurationService;
import rocks.milspecsg.msreferral.service.common.data.registry.MSReferralRegistry;
import rocks.milspecsg.msreferral.service.common.member.CommonMemberManager;
import rocks.milspecsg.msreferral.service.common.member.repository.CommonMongoMemberRepository;
import rocks.milspecsg.msrepository.api.data.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.api.datastore.MongoContext;
import rocks.milspecsg.msrepository.api.manager.annotation.MongoDBComponent;
import rocks.milspecsg.msrepository.api.misc.BindingExtensions;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.common.misc.CommonBindingExtensions;

@SuppressWarnings("UnstableApiUsage")
public class CommonModule<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends AbstractModule {

    @Override
    protected void configure() {
        BindingExtensions be = new CommonBindingExtensions(binder());

        be.bind(
            new TypeToken<MemberRepository<?, ?>>(getClass()) {
            },
            new TypeToken<MemberRepository<ObjectId, Datastore>>(getClass()) {
            },
            new TypeToken<CommonMongoMemberRepository>(getClass()) {
            },
            MongoDBComponent.class
        );

        be.bind(
            new TypeToken<MemberManager<TString>>(getClass()) {
            },
            new TypeToken<CommonMemberManager<TUser, TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<PluginInfo<TString>>(getClass()) {
            },
            new TypeToken<MSReferralPluginInfo<TString, TCommandSource>>(getClass()) {
            }
        );

        bind(new TypeLiteral<DataStoreContext<ObjectId, Datastore>>() {
        }).to(new TypeLiteral<MongoContext>() {
        });

        bind(ConfigurationService.class).to(MSReferralConfigurationService.class);
        bind(Registry.class).to(MSReferralRegistry.class);
    }
}
