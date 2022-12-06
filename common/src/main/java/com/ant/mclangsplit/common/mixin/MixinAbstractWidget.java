package com.ant.mclangsplit.common.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractWidget.class)
public abstract class MixinAbstractWidget extends GuiComponent {
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    @Shadow
    private int x;
    @Shadow
    private int y;
    @Shadow
    private Component message;
    @Shadow
    protected boolean isHovered;
    @Shadow
    public boolean active;
    @Shadow
    protected float alpha;
    @Shadow
    private boolean focused;

    @Shadow
    abstract int getYImage(boolean bl);
    @Shadow
    abstract void renderBg(PoseStack poseStack, Minecraft minecraft, int i, int j);

    @Inject(at = @At("HEAD"), method = "renderButton", cancellable = true)
    public void renderButton(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, AbstractWidget.WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        int k = this.getYImage(this.isHovered || this.focused);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, (int)x, (int)y, 0, 46 + k * 20, this.width / 2, this.height);
        this.blit(poseStack, (int)(x + this.width / 2), (int)y, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
        this.renderBg(poseStack, minecraft, i, j);
        int l = this.active ? 0xFFFFFF : 0xA0A0A0;

        int w = font.width(message);
        float sf = w > width - 16 ? 1f / (w / (width - 16f)) : 1f;

        poseStack.pushPose();
        poseStack.scale(sf, 1f, 1f);
        AbstractWidget.drawCenteredString(poseStack, font, message.copy(), (int)(((float)this.x + (float)this.width / 2f) * (1f / sf)), (int)(y + (this.height - 8) / 2), l | Mth.ceil(this.alpha * 255.0f) << 24);
        poseStack.popPose();

        ci.cancel();
    }
}
