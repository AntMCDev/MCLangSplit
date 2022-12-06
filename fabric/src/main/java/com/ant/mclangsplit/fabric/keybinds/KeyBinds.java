package com.ant.mclangsplit.fabric.keybinds;

import com.ant.mclangsplit.common.translation.Storage;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final KeyMapping cycle = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mclangsplit.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.mclangsplit"));

    public static void init() {
        ScreenEvents.BEFORE_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
            ScreenKeyboardEvents.allowKeyPress(screen).register(((screen1, key, scancode, modifiers) -> true));
            ScreenKeyboardEvents.beforeKeyPress(screen).register(((screen1, key, scancode, modifiers) -> {
                if (cycle.matches(key, scancode)) {
                    cycle.setDown(true);
                    cycle.clickCount++;
                }
            }));
            ScreenKeyboardEvents.beforeKeyRelease(screen).register(((screen1, key, scancode, modifiers) -> {
                if (cycle.matches(key, scancode)) {
                    cycle.setDown(false);
                }
            }));
            ScreenEvents.beforeTick(screen).register((screen1 -> {
                boolean b = false;
                while (cycle.consumeClick()) {
                    b = true;
                }

                if (b) {
                    Storage.cycle();
                }
            }));
        }));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean b = false;
            while (cycle.consumeClick()) {
                b = true;
            }

            if (b) {
                Storage.cycle();
            }
        });
    }
}
