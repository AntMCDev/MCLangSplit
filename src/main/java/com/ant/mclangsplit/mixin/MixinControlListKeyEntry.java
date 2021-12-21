package com.ant.mclangsplit.mixin;

import com.ant.mclangsplit.MCLangSplit;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.controls.ControlList;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@OnlyIn(Dist.CLIENT)
@Mixin(ControlList.KeyEntry.class)
public class MixinControlListKeyEntry {
    @Final
    @Shadow
    private KeyMapping key;
    @Final
    @Shadow
    private Component name;
    @Final
    @Shadow
    private Button changeButton;
    @Final
    @Shadow
    private Button resetButton;

    @Shadow(aliases = { "this$0", "a" })
    private ControlList this$0;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(PoseStack p_97463_, int p_97464_, int p_97465_, int p_97466_, int p_97467_, int p_97468_, int p_97469_, int p_97470_, boolean p_97471_, float p_97472_, CallbackInfo ci) {
        ControlsScreen controlsScreen = ((MixinControlListAccessor)this$0).getControlsScreen();
        int maxNameWidth = ((MixinControlListAccessor)this$0).getMaxNameWidth();
        Minecraft mc = Minecraft.getInstance();

        float width = mc.screen.width / 2;
        int strWidth = mc.font.width(name);
        float scaleFactor = strWidth > width - 6 ? 1f / (strWidth / (width - 6f)) : 1f;

        boolean flag = controlsScreen.selectedKey == this.key;
        float f = (float)(p_97466_ + 90 - maxNameWidth);
        p_97463_.pushPose();
        p_97463_.scale(scaleFactor, 1f, 1f);
        mc.font.draw(p_97463_, this.name, 6f, (float)(p_97465_ + p_97468_ / 2 - 9 / 2), 16777215);
        p_97463_.popPose();
        this.resetButton.x = p_97466_ + 190 + 20;
        this.resetButton.y = p_97465_;
        this.resetButton.active = !this.key.isDefault();
        this.resetButton.render(p_97463_, p_97469_, p_97470_, p_97472_);
        this.changeButton.x = p_97466_ + 105;
        this.changeButton.y = p_97465_;
        this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
        boolean flag1 = false;
        boolean keyCodeModifierConflict = true;
        if (!this.key.isUnbound()) {
            for(KeyMapping keymapping : mc.options.keyMappings) {
                if (keymapping != this.key && this.key.same(keymapping)) {
                    flag1 = true;
                    keyCodeModifierConflict &= keymapping.hasKeyCodeModifierConflict(keymapping);
                }
            }
        }

        if (flag) {
            this.changeButton.setMessage((new TextComponent("> ")).append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW)).append(" <").withStyle(ChatFormatting.YELLOW));
        } else if (flag1) {
            this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(keyCodeModifierConflict ? ChatFormatting.GOLD : ChatFormatting.RED));
        }

        this.changeButton.render(p_97463_, p_97469_, p_97470_, p_97472_);

        ci.cancel();
    }
}
