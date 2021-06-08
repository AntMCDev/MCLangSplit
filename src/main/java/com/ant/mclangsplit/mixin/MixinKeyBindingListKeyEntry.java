package com.ant.mclangsplit.mixin;

import com.ant.mclangsplit.MCLangSplit;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.KeyBindingList;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(KeyBindingList.KeyEntry.class)
public class MixinKeyBindingListKeyEntry {
    @Final
    @Shadow
    private KeyBinding key;
    @Final
    @Shadow
    private ITextComponent name;
    @Final
    @Shadow
    private Button changeButton;
    @Final
    @Shadow
    private Button resetButton;

    @Shadow(aliases = { "this$0", "field_148284_a" })
    private KeyBindingList this$0;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_, CallbackInfo ci) {
        try {
            Field controlsScreenField = ObfuscationReflectionHelper.findField(KeyBindingList.class, "field_148191_k");
            Field maxNameWidthField = ObfuscationReflectionHelper.findField(KeyBindingList.class, "field_148188_n");
            if (!controlsScreenField.isAccessible()) {
                controlsScreenField.setAccessible(true);
            }
            if (!maxNameWidthField.isAccessible()) {
                maxNameWidthField.setAccessible(true);
            }

            ControlsScreen controlsScreen = (ControlsScreen)controlsScreenField.get(this$0);
            int maxNameWidth = (int)maxNameWidthField.get(this$0);

            Minecraft mc = Minecraft.getInstance();

            float width = mc.screen.width / 2;
            int strWidth = mc.font.width(name);
            float scaleFactor = strWidth > width - 6 ? 1f / (strWidth / (width - 6f)) : 1f;

            boolean flag = controlsScreen.selectedKey == this.key;
            p_230432_1_.pushPose();
            p_230432_1_.scale(scaleFactor, 1f, 1f);
            mc.font.draw(p_230432_1_, this.name, 6f, (float)(p_230432_3_ + p_230432_6_ / 2 - 9 / 2), 16777215);
            p_230432_1_.popPose();
            this.resetButton.x = p_230432_4_ + 190 + 20;
            this.resetButton.y = p_230432_3_;
            this.resetButton.active = !this.key.isDefault();
            this.resetButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
            this.changeButton.x = p_230432_4_ + 105;
            this.changeButton.y = p_230432_3_;
            this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
            boolean flag1 = false;
            boolean keyCodeModifierConflict = true;
            if (!this.key.isUnbound()) {
                for(KeyBinding keybinding : mc.options.keyMappings) {
                    if (keybinding != this.key && this.key.same(keybinding)) {
                        flag1 = true;
                        keyCodeModifierConflict &= keybinding.hasKeyCodeModifierConflict(keybinding);
                    }
                }
            }

            if (flag) {
                this.changeButton.setMessage((new StringTextComponent("> ")).append(this.changeButton.getMessage().copy().withStyle(TextFormatting.YELLOW)).append(" <").withStyle(TextFormatting.YELLOW));
            } else if (flag1) {
                this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(keyCodeModifierConflict ? TextFormatting.GOLD : TextFormatting.RED));
            }

            this.changeButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);

            ci.cancel();

        } catch (IllegalAccessException ex) {
            MCLangSplit.LOGGER.error(ex.getMessage());
        }
    }
}
