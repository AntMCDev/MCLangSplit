package com.ant.mclangsplit.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(KeyBindsList.KeyEntry.class)
public class MixinKeyBindsListKeyEntry {
    @Final @Shadow
    private KeyMapping key;
    @Final @Shadow
    private Component name;
    @Final @Shadow
    private Button changeButton;
    @Final @Shadow
    private Button resetButton;

    @Shadow(aliases = { "this$0", "field_2742", "a" })
    private KeyBindsList this$0;

    @Shadow
    private boolean hasCollision;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Font var10001 = minecraft.font;
        Component var10002 = this.name.copy();
        int var10003 = k + 90 - this$0.maxNameWidth;
        int var10004 = j + m / 2;
        Objects.requireNonNull(minecraft.font);
        guiGraphics.drawString(var10001, var10002, var10003, var10004 - 9 / 2, 16777215, false);
        this.resetButton.setX(k + 190);
        this.resetButton.setY(j);
        this.resetButton.render(guiGraphics, n, o, f);
        this.changeButton.setX(k + 105);
        this.changeButton.setY(j);
        if (this.hasCollision) {
            boolean p = true;
            int q = this.changeButton.getX() - 6;
            guiGraphics.fill(q, j + 2, q + 3, j + m + 2, ChatFormatting.RED.getColor() | -16777216);
        }

        this.changeButton.render(guiGraphics, n, o, f);

        ci.cancel();
    }
}
