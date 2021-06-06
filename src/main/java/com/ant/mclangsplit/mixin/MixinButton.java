package com.ant.mclangsplit.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Button.class)
public abstract class MixinButton extends AbstractButton {
    public MixinButton(int p_i232251_1_, int p_i232251_2_, int p_i232251_3_, int p_i232251_4_, ITextComponent p_i232251_5_) {
        super(p_i232251_1_, p_i232251_2_, p_i232251_3_, p_i232251_4_, p_i232251_5_);
    }

    @Inject(at = @At("HEAD"), method = "renderButton", cancellable = true)
    public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_, CallbackInfo ci) {
        if (this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            this.isHovered = p_230431_2_ >= this.x && p_230431_3_ >= this.y && p_230431_2_ < this.x + this.width && p_230431_3_ < this.y + this.height;
            int k = this.getYImage(this.isHovered());
            GuiUtils.drawContinuousTexturedBox(p_230431_1_, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
            this.renderBg(p_230431_1_, mc, p_230431_2_, p_230431_3_);

            ITextComponent buttonText = this.getMessage();
            int strWidth = mc.font.width(buttonText);

            float scaleFactor = strWidth > width - 6 ? 1f / (strWidth / (width - 6f)) : 1f;

            p_230431_1_.pushPose();
            p_230431_1_.scale(scaleFactor, 1f, 1f);
            drawCenteredString(p_230431_1_, mc.font, buttonText, (int)(((float)this.x + (float)this.width / 2f) * (1f / scaleFactor)), this.y + (this.height - 8) / 2, getFGColor());
            p_230431_1_.popPose();
        }
        ci.cancel();
    }
}
