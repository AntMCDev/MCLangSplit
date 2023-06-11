package com.ant.mclangsplit.forge;

import com.ant.mclangsplit.common.config.ConfigFileAccessor;
import com.ant.mclangsplit.forge.config.ClothConfig;
import com.ant.mclangsplit.forge.keybinds.KeyBinds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;


@Mod(com.ant.mclangsplit.common.MCLangSplit.MOD_NAME)
@Mod.EventBusSubscriber(modid = com.ant.mclangsplit.common.MCLangSplit.MOD_NAME, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MCLangSplit {
    public MCLangSplit(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        bus.addListener(KeyBinds::init);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(KeyBinds::tick);
        MinecraftForge.EVENT_BUS.addListener(KeyBinds::screenKeydown);
        MinecraftForge.EVENT_BUS.addListener(KeyBinds::screenKeydown);
    }

    private void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ConfigFileAccessor.path = FMLPaths.CONFIGDIR.get();
            ConfigFileAccessor.load(); ConfigFileAccessor.save();
            com.ant.mclangsplit.common.MCLangSplit.init();
            ClothConfig.screen();
        });
    }
}
