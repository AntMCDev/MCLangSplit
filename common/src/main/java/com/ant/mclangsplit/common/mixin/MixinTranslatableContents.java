package com.ant.mclangsplit.common.mixin;

import com.ant.mclangsplit.common.config.Config;
import com.ant.mclangsplit.common.translation.Storage;
import com.google.common.collect.ImmutableList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.contents.TranslatableFormatException;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Mixin(TranslatableContents.class)
public abstract class MixinTranslatableContents {
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

    @Final @Mutable @Shadow
    private String key;

    @Final @Mutable @Shadow
    private Object[] args;

    private Config.Mode mode = Config.Mode.ORIGINAL;

    @Shadow
    private Language decomposedWith;

    @Shadow
    private List<FormattedText> decomposedParts;

    @Shadow
    abstract void decomposeTemplate(String string, Consumer<FormattedText> consumer);

    @Inject(at = @At("HEAD"), method = "decompose", cancellable = true)
    private void decompose(CallbackInfo ci) {
        Language defaultLanguage = Language.getInstance();
        Language secondLanguage = Storage.secondLanguage;
        if (defaultLanguage == this.decomposedWith && this.mode == Config.INSTANCE.mode) {
            return;
        }
        this.decomposedWith = defaultLanguage;
        this.mode = Config.INSTANCE.mode;

        boolean isTooltip = this.key.startsWith("TOOLTIP");
        if (isTooltip) {
            this.key = this.key.replace("TOOLTIP", "");
        }

        if (Storage.secondLanguage == null || useDefault() || (Config.INSTANCE.ignoreTooltips && isTooltip)) {
            this.decomposedParts = decompose(defaultLanguage);
            return;
        }

        List<FormattedText> list = new ArrayList<>();
        switch (Config.INSTANCE.mode) {
            case ORIGINAL -> list = decompose(defaultLanguage);
            case ALTERNATE -> {
                list = secondLanguage.has(this.key) ? decompose(secondLanguage) : decompose(defaultLanguage);
            }
            case BOTH -> {
                list = decompose(defaultLanguage);
                if (secondLanguage.has(this.key)) {
                    Object[] tmp = new Object[args.length*2];
                    System.arraycopy(args, 0, tmp, 0, args.length);
                    System.arraycopy(args, 0, tmp, args.length, args.length);
                    args = tmp;

                    List<FormattedText> secondary = decompose(secondLanguage);
                    boolean match = list.size() == secondary.size();
                    for (int i = 0; i < list.size() && match; i++) {
                        match = list.get(i).getString().equals(secondary.get(i).getString());
                    }
                    if (!match) {
                        list.add(FormattedText.of(" "));
                        list.addAll(secondary);
                    }
                }
            }
        }
        this.decomposedParts = list;
    }

    private List<FormattedText> decompose(Language language) {
        String string = language.getOrDefault(this.key);
        try {
            List<FormattedText> list = new ArrayList<>();
            this.decomposeTemplate(string, list::add);
            return list;
        }
        catch (TranslatableFormatException translatableFormatException) {
            return List.of(FormattedText.of(string));
        }
    }

    private boolean useDefault() {
        if (IGNORE_DUAL_TRANSLATION_KEYS.contains(this.key)) {
            return true;
        }

        if (Arrays.asList(Config.INSTANCE.includeKeys).contains(this.key)) {
            return false;
        }

        for (String s : Config.INSTANCE.includeKeys) {
            if (s.endsWith("*") && this.key.startsWith(s.substring(0, s.length() - 1))) {
                return false;
            }
        }

        if (Arrays.asList(Config.INSTANCE.ignoreKeys).contains(this.key)) {
            return true;
        }

        for (String s : Config.INSTANCE.ignoreKeys) {
            if (s.endsWith("*") && this.key.startsWith(s.substring(0, s.length() - 1))) {
                return true;
            }
        }

        return false;
    }
}
