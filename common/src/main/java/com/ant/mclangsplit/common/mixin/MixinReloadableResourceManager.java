package com.ant.mclangsplit.common.mixin;

import com.ant.mclangsplit.common.MCLangSplit;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManager.class)
public class MixinReloadableResourceManager {
    @Inject(at = @At("RETURN"), method = "createReload")
    public void createReload(Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, List<PackResources> list, CallbackInfoReturnable<ReloadInstance> cir) {
        MCLangSplit.init();
    }
}
