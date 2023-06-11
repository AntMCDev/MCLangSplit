package com.ant.mclangsplit.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public class MixinScreen {
    @Inject(at = @At("RETURN"), method = "getTooltipFromItem", cancellable = true)
    private static void getTooltipFromItem(Minecraft minecraft, ItemStack itemStack, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> result = cir.getReturnValue();
        if (result != null) {
            for (Component c : result) {
                if (c instanceof TranslatableContents tc) {
                    ((MixinTranslatableContentsAccessor) tc).setKey("TOOLTIP" + tc.getKey());
                }
            }
            cir.setReturnValue(result);
        }
    }
}
