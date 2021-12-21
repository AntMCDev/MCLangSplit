package com.ant.mclangsplit.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Client {
        public final ForgeConfigSpec.ConfigValue<String> languageSetting;
        public final ForgeConfigSpec.ConfigValue<List<String>> ignoreKeys;
        public final ForgeConfigSpec.ConfigValue<List<String>> includeKeys;
        public final ForgeConfigSpec.ConfigValue<Boolean> ignoreTooltips;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("Language Settings");
            languageSetting = builder.comment("Set this to the second language").define("secondlanguage", "");
            ignoreKeys = builder.comment("Use this to ignore a set of language keys for dual translation").define("ignoreKeys", new ArrayList<>());
            includeKeys = builder.comment("Use this to include a set of language keys, bypassing the ignoreKeys property").define("includeKeys", new ArrayList<>());
            ignoreTooltips = builder.comment("Use this to enable/disable showing dual translations on tooltips").define("ignoreTooltips", false);
            builder.pop();
        }
    }
}
