package com.ant.mclangsplit.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<String> languageSetting;
        public final ForgeConfigSpec.ConfigValue<List<String>> ignoreKeys;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Language Settings");
            languageSetting = builder.comment("Set this to the second language").define("secondlanguage", "");
            List<String> defaultIgnoreKeys = new ArrayList<>();
            defaultIgnoreKeys.add("options.on.composed");
            ignoreKeys = builder.comment("Use this to ignore a set of language keys for dual translation").define("ignoreKeys", defaultIgnoreKeys);
            builder.pop();
        }
    }
}
