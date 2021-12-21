package com.ant.mclangsplit.mixin;

import com.ant.mclangsplit.ClientLanguageExtension;
import com.ant.mclangsplit.MCLangSplit;
import com.ant.mclangsplit.config.ConfigHandler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.metadata.language.LanguageMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientLanguage.class)
public abstract class MixinClientLanguage {
    @Invoker("appendFrom")
    private static void appendFrom(List<Resource> p_118922_, Map<String, String> p_118923_) {
        throw new AssertionError();
    }

    @Inject(at = @At("RETURN"), method="loadFrom", cancellable = true)
    private static void loadFrom(ResourceManager p_118917_, List<LanguageInfo> p_118918_, CallbackInfoReturnable<ClientLanguage> cir) {
        Map<String, String> map = Maps.newHashMap();
        boolean flag = false;

        Map<String, LanguageInfo> langMap = extractLanguages(p_118917_.listPacks());
        if (langMap.containsKey(ConfigHandler.CLIENT.languageSetting.get())) {
            LanguageInfo languageinfo = langMap.get(ConfigHandler.CLIENT.languageSetting.get());
            flag |= languageinfo.isBidirectional();
            String s = String.format("lang/%s.json", languageinfo.getCode());

            for(String s1 : p_118917_.getNamespaces()) {
                try {
                    ResourceLocation resourcelocation = new ResourceLocation(s1, s);
                    appendFrom(p_118917_.getResources(resourcelocation), map);
                } catch (FileNotFoundException filenotfoundexception) {
                } catch (Exception exception) {
                    MCLangSplit.LOGGER.warn("Skipped language file: {}:{} ({})", s1, s, exception.toString());
                }
            }
        }

        Constructor<?> constructor = ObfuscationReflectionHelper.findConstructor(ClientLanguage.class, Map.class, Boolean.TYPE);
        if (Modifier.isPrivate(constructor.getModifiers())) {
            constructor.setAccessible(true);
        }
        try {
            ClientLanguageExtension.altTranslations = (ClientLanguage)constructor.newInstance(ImmutableMap.copyOf(map), flag);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            MCLangSplit.LOGGER.error("Could not access constructor in ClientLanguageMap injection", ex);
        }
    }

    private static Map<String, LanguageInfo> extractLanguages(Stream<PackResources> p_118982_) {
        Map<String, LanguageInfo> map = Maps.newHashMap();
        p_118982_.forEach((p_118980_) -> {
            try {
                LanguageMetadataSection languagemetadatasection = p_118980_.getMetadataSection(LanguageMetadataSection.SERIALIZER);
                if (languagemetadatasection != null) {
                    for(LanguageInfo languageinfo : languagemetadatasection.getLanguages()) {
                        map.putIfAbsent(languageinfo.getCode(), languageinfo);
                    }
                }
            } catch (IOException | RuntimeException runtimeexception) {
                MCLangSplit.LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", p_118980_.getName(), runtimeexception);
            }

        });
        return ImmutableMap.copyOf(map);
    }
}
