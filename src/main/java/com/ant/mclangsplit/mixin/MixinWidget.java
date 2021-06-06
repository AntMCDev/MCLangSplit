package com.ant.mclangsplit.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Widget.class)
public abstract class MixinWidget extends AbstractGui implements IRenderable, IGuiEventListener {
    @Shadow
    public boolean visible;
    @Shadow
    protected boolean isHovered;
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    abstract void renderBg(MatrixStack p_230441_1_, Minecraft p_230441_2_, int p_230441_3_, int p_230441_4_);
    @Shadow
    abstract int getYImage(boolean p_230989_1_);
    @Shadow
    abstract ITextComponent getMessage();
    @Shadow
    abstract int getFGColor();

    @Inject(at = @At("HEAD"), method = "renderButton", cancellable = true)
    public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_, CallbackInfo ci) {
        if (this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            this.isHovered = p_230431_2_ >= this.x && p_230431_3_ >= this.y && p_230431_2_ < this.x + this.width && p_230431_3_ < this.y + this.height;
            int k = this.getYImage(this.isHovered);
            GuiUtils.drawContinuousTexturedBox(p_230431_1_, Widget.WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
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
