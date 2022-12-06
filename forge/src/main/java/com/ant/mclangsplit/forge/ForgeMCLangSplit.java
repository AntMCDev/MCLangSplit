package com.ant.mclangsplit.forge;

import com.ant.mclangsplit.common.MCLangSplit;
import com.ant.mclangsplit.forge.ClothConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;


@Mod(MCLangSplit.MOD_NAME)
@Mod.EventBusSubscriber(modid = MCLangSplit.MOD_NAME, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeMCLangSplit {
    public ForgeMCLangSplit(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ConfigFileAccessor.path = FMLPaths.CONFIGDIR.get();
            ConfigFileAccessor.load(); ConfigFileAccessor.save();
            MCLangSplit.init();
            ClothConfig.screen();
        });
    }
}
