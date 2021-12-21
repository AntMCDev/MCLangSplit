package com.ant.mclangsplit.mixin;

import net.minecraft.client.gui.screens.controls.ControlList;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@OnlyIn(Dist.CLIENT)
@Mixin(ControlList.class)
public interface MixinControlListAccessor {
    @Final
    @Accessor
    ControlsScreen getControlsScreen();

    @Accessor
    int getMaxNameWidth();
}
