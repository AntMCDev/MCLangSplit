package com.ant.mclangsplit;

import com.ant.mclangsplit.config.ConfigHandler;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(MCLangSplit.MOD_ID)
public class MCLangSplit {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "mclangsplit";
    public static KeyMapping keyMapping;

    public MCLangSplit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        keyMapping = new KeyMapping("key.mclangsplit.toggle", GLFW.GLFW_KEY_R, "category.mclangsplit");
        ClientRegistry.registerKeyBinding(keyMapping);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if (event.getKey() == keyMapping.getKey().getValue() && event.getAction() == GLFW.GLFW_RELEASE) {
                ClientLanguageExtension.nextMode();
            }
        }
    }
}
