package rocks.milspecsg.msreferral;

import com.google.inject.Inject;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.api.util.StringResult;

public class MSReferralPluginInfo<TString, TCommandSource> implements PluginInfo<TString> {
    public static final String id = "msreferral";
    public static final String name = "MSReferral";
    public static final String version = "$modVersion";
    public static final String description = "A Simple referral plugin";
    public static final String url = "https://github.com/MilSpecSG/MSReferral";
    public static final String authors = "STG_Allen";
    public TString pluginPrefix;

    @Inject
    public void setPluginPrefix(StringResult<TString, TCommandSource> stringResult) {
        pluginPrefix = stringResult.builder().gold().append("[", name, "]").build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getAuthors() {
        return authors;
    }

    @Override
    public TString getPrefix() {
        return pluginPrefix;
    }
}
