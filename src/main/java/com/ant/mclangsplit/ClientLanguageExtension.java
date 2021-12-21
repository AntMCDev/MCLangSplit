package com.ant.mclangsplit;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientLanguageExtension {
    public enum Mode {
        SHOW_ORIGINAL, SHOW_ALTERNATE, SHOW_BOTH
    }

    public static ClientLanguage altTranslations = null;
    public static ClientLanguageExtension.Mode translationMode = Mode.SHOW_BOTH;

    public static void nextMode() {
        switch (translationMode) {
            case SHOW_BOTH -> translationMode = Mode.SHOW_ORIGINAL;
            case SHOW_ORIGINAL -> translationMode = Mode.SHOW_ALTERNATE;
            case SHOW_ALTERNATE -> translationMode = Mode.SHOW_BOTH;
        }
    }
}
