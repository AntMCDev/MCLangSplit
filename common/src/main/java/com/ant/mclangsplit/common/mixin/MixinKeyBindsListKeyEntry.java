package com.ant.mclangsplit.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(PoseStack poseStack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        float w = minecraft.screen.width / 2f;
        int sw = minecraft.font.width(name);
        float sf = sw > w - 6 ? 1f / (sw / (w - 6f)) : 1f;
        poseStack.pushPose();
        poseStack.scale(sf, 1f, 1f);
        minecraft.font.draw(poseStack, this.name.copy(), 6f, (float)(j + m / 2 - minecraft.font.lineHeight / 2), 0xFFFFFF);
        poseStack.popPose();

        boolean bl2 = this$0.keyBindsScreen.selectedKey == this.key;
        this.resetButton.setX(k + 190);
        this.resetButton.setY(j);
        this.resetButton.active = !this.key.isDefault();
        this.resetButton.render(poseStack, n, o, f);
        this.changeButton.setX(k + 105);
        this.changeButton.setY(j);
        this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
        boolean bl3 = false;
        if (!this.key.isUnbound()) {
            for (KeyMapping keyMapping : minecraft.options.keyMappings) {
                if (keyMapping == this.key || !this.key.same(keyMapping)) continue;
                bl3 = true;
                break;
            }
        }
        if (bl2) {
            this.changeButton.setMessage(Component.literal("> ").append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW)).append(" <").withStyle(ChatFormatting.YELLOW));
        } else if (bl3) {
            this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(ChatFormatting.RED));
        }
        this.changeButton.render(poseStack, n, o, f);

        ci.cancel();
    }
}
