package com.ant.mclangsplit.fabric;

import com.ant.mclangsplit.common.config.ConfigFileAccessor;
import com.ant.mclangsplit.fabric.keybinds.KeyBinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class MCLangSplit implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConfigFileAccessor.path = FabricLoader.getInstance().getConfigDir();
		ConfigFileAccessor.load(); ConfigFileAccessor.save();
		KeyBinds.init();
	}
}
