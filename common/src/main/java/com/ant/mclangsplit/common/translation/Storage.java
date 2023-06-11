package com.ant.mclangsplit.common.translation;

import com.ant.mclangsplit.common.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.metadata.language.LanguageMetadataSection;

import java.io.IOException;
import java.util.Collections;

public class Storage {
    public static ClientLanguage secondLanguage = null;

    public static void cycle() {
        switch (Config.INSTANCE.mode) {
            case BOTH -> Config.INSTANCE.mode = Config.Mode.ORIGINAL;
            case ORIGINAL -> Config.INSTANCE.mode = Config.Mode.ALTERNATE;
            case ALTERNATE -> Config.INSTANCE.mode = Config.Mode.BOTH;
        }
    }

    public static void load() {
        Minecraft.getInstance().getResourceManager().listPacks().forEach(p -> {
            try {
                LanguageMetadataSection m = p.getMetadataSection(LanguageMetadataSection.TYPE);
                if (m != null) {
                    m.languages().keySet().forEach(l -> {
                        if (l.equals(Config.INSTANCE.language)) {
                            Storage.secondLanguage = ClientLanguage.loadFrom(
                                    Minecraft.getInstance().getResourceManager(),
                                    Collections.singletonList(l),
                                    m.languages().get(l).bidirectional()
                            );
                        }
                    });
                }
            } catch (IOException ignore) { }
        });
    }
}
