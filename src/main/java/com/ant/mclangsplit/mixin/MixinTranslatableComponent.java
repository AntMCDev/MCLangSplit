package com.ant.mclangsplit.mixin;

import com.ant.mclangsplit.ClientLanguageExtension;
import com.ant.mclangsplit.config.ConfigHandler;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TranslatableFormatException;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(TranslatableComponent.class)
public abstract class MixinTranslatableComponent {
    private static final List<String> IGNORE_DUAL_TRANSLATION_KEYS = new ArrayList<>();
    static {
        IGNORE_DUAL_TRANSLATION_KEYS.add("translation.test.invalid");
        IGNORE_DUAL_TRANSLATION_KEYS.add("translation.test.invalid2");
        IGNORE_DUAL_TRANSLATION_KEYS.add("options.on.composed");
        IGNORE_DUAL_TRANSLATION_KEYS.add("options.off.composed");
        IGNORE_DUAL_TRANSLATION_KEYS.add("options.generic_value");
        IGNORE_DUAL_TRANSLATION_KEYS.add("options.pixel_value");
        IGNORE_DUAL_TRANSLATION_KEYS.add("options.percent_value");
        IGNORE_DUAL_TRANSLATION_KEYS.add("options.percent_add_value");
    }

    @Final
    @Shadow
    @Mutable
    private String key;

    @Shadow
    private Language decomposedWith;

    @Final
    @Shadow
    private List<FormattedText> decomposedParts;

    @Shadow
    abstract void decomposeTemplate(String p_131322_);

    @Inject(at = @At("HEAD"), method = "decompose", cancellable = true)
    private void decompose(CallbackInfo ci) {
        // Determine if the text source is from a tooltip (injected via MixinScreen.getTooltipFromItem)
        boolean fromTooltip = false;
        if (this.key.startsWith("TOOLTIP")) {
            fromTooltip = true;
            this.key = this.key.replace("TOOLTIP", "");
        }

        // Check if the key is excluded from dual translation via the config
        boolean excludeKey = shouldExcludeKey(this.key);

        Language language = Language.getInstance();

        this.decomposedWith = language;
        this.decomposedParts.clear();
        String s = language.getOrDefault(this.key);

        // Determine if it should show the default translation
        boolean isShowingDefault = shouldShowDefaultTranslation() || (ConfigHandler.CLIENT.ignoreTooltips.get() && fromTooltip) || excludeKey;
        try {
            if (isShowingDefault) {
                // Add the default translation
                this.decomposeTemplate(s);
            }

            // Determine if it should show the alternate translation
            if (ClientLanguageExtension.altTranslations != null && shouldShowAltTranslation() && !excludeKey && !(ConfigHandler.CLIENT.ignoreTooltips.get() && fromTooltip)) {
                String alt = ClientLanguageExtension.altTranslations.getOrDefault(this.key);
                if (!s.equals(alt)) {
                    // If the values don't match add the alternate translation (prefixed with a space if both showing)
                    if (ClientLanguageExtension.translationMode == ClientLanguageExtension.Mode.SHOW_BOTH) {
                        this.decomposedParts.add(FormattedText.of(" "));
                    }
                    this.decomposeTemplate(alt);
                } else if (!isShowingDefault) {
                    // If values match and the default isn't showing, add the original translation as a fallback - usually only the case when both are null
                    this.decomposeTemplate(s);
                }
            }
        } catch (TranslatableFormatException translatableformatexception) {
            // Clear the translation and add the original text as a fallback
            this.decomposedParts.clear();
            this.decomposedParts.add(FormattedText.of(s));
        }

        ci.cancel();
    }

    private boolean shouldExcludeKey(String key) {
        if (IGNORE_DUAL_TRANSLATION_KEYS.contains(this.key)) {
            return true;
        }

        if (ConfigHandler.CLIENT.includeKeys.get().contains(key)) {
            return false;
        }

        for (String s : ConfigHandler.CLIENT.includeKeys.get()) {
            if (s.endsWith("*")) {
                if (key.startsWith(s.substring(0, s.length() - 1))) {
                    return false;
                }
            }
        }

        if (ConfigHandler.CLIENT.ignoreKeys.get().contains(key)) {
            return true;
        }

        for (String s : ConfigHandler.CLIENT.ignoreKeys.get()) {
            if (s.endsWith("*")) {
                if (key.startsWith(s.substring(0, s.length() - 1))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean shouldShowDefaultTranslation() {
        return ClientLanguageExtension.translationMode == ClientLanguageExtension.Mode.SHOW_ORIGINAL ||
                ClientLanguageExtension.translationMode == ClientLanguageExtension.Mode.SHOW_BOTH;
    }

    private boolean shouldShowAltTranslation() {
        return ClientLanguageExtension.translationMode == ClientLanguageExtension.Mode.SHOW_ALTERNATE ||
                ClientLanguageExtension.translationMode == ClientLanguageExtension.Mode.SHOW_BOTH;
    }
}
