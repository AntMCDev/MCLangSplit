package com.ant.mclangsplit.forge.keybinds;

import com.ant.mclangsplit.common.translation.Storage;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final KeyMapping cycle = new KeyMapping("key.mclangsplit.toggle", GLFW.GLFW_KEY_R, "category.mclangsplit");
    public static void init(RegisterKeyMappingsEvent event) {
        event.register(cycle);
    }

    public static void tick(final TickEvent.ClientTickEvent event) {
        boolean b = false;
        while (cycle.consumeClick()) {
            b = true;
        }

        if (b) {
            Storage.cycle();
        }
    }

    public static void screenKeydown(final ScreenEvent.KeyPressed event) {
        if (cycle.matches(event.getKeyCode(), event.getScanCode())) {
            cycle.setDown(true);
            cycle.clickCount++;
        }
    }

    public static void screenKeyup(final ScreenEvent.KeyReleased event) {
        if (cycle.matches(event.getKeyCode(), event.getScanCode())) {
            cycle.setDown(false);
        }
    }
}
