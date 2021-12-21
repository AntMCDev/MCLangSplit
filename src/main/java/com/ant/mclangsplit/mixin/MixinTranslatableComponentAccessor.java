package com.ant.mclangsplit.mixin;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@OnlyIn(Dist.CLIENT)
@Mixin (TranslatableComponent.class)
public interface MixinTranslatableComponentAccessor {
    @Final
    @Mutable
    @Accessor("key")
    void setKey(String key);
}
