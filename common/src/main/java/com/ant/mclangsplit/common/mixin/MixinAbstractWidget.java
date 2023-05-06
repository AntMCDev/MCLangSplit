package com.ant.mclangsplit.common.mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractWidget.class)
public abstract class MixinAbstractWidget {
    @Shadow
    private Component message;

    @Inject(at = @At("RETURN"), method = "getMessage", cancellable = true)
    public void getMessage(CallbackInfoReturnable<Component> cir) {
        cir.setReturnValue(message.copy());
    }
}
