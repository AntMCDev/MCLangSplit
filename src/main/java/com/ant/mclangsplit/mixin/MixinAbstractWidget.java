package com.ant.mclangsplit.mixin;

import com.ant.mclangsplit.MCLangSplit;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(AbstractWidget.class)
public abstract class MixinAbstractWidget extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
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
    public float alpha;
    @Shadow
    abstract void renderBg(PoseStack p_230441_1_, Minecraft p_230441_2_, int p_230441_3_, int p_230441_4_);
    @Shadow
    abstract int getYImage(boolean p_230989_1_);
    @Shadow
    abstract Component getMessage();
    @Shadow
    abstract int getFGColor();
    @Shadow
    abstract boolean isHovered();

    @Inject(at = @At("HEAD"), method = "renderButton", cancellable = true)
    public void renderButton(PoseStack p_93676_, int p_93677_, int p_93678_, float p_93679_, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        Component buttonText = this.getMessage();
        int strWidth = font.width(buttonText);
        float scaleFactor = strWidth > width - 6 ? 1f / (strWidth / (width - 6f)) : 1f;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, AbstractWidget.WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(p_93676_, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.blit(p_93676_, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(p_93676_, minecraft, p_93677_, p_93678_);
        int j = getFGColor();
        p_93676_.pushPose();
        p_93676_.scale(scaleFactor, 1f, 1f);
        drawCenteredString(p_93676_, font, this.getMessage().plainCopy(), (int)(((float)this.x + (float)this.width / 2f) * (1f / scaleFactor)), this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
        p_93676_.popPose();

        ci.cancel();
    }
}
