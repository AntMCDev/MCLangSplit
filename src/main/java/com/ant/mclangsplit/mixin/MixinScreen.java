package com.ant.mclangsplit.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(Screen.class)
public class MixinScreen {
    @Inject(at = @At("HEAD"), method = "getTooltipFromItem", cancellable = true)
    public void getTooltipFromItem(ItemStack p_96556_, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> result = cir.getReturnValue();
        for (Component c : result) {
            modify(c);
        };
        cir.setReturnValue(result);
    }

    private void modify(Component c) {
        if (c instanceof TranslatableComponent) {
            ((MixinTranslatableComponentAccessor)c).setKey("TOOLTIP"+((TranslatableComponent)c).getKey());
        }
        for (Component c2 : c.getSiblings()) {
            modify(c2);
        }
    }
}
