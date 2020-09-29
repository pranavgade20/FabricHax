package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.AntiKnockback;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class KnockbackManager {
    @Inject(at = @At("HEAD"), method = "takeKnockback(FDD)V", cancellable = true)
    private void handleKnockback(float f, double d, double e, CallbackInfo ci) {
        //TODO this doesnt work, ignore velocity update packets
        if (AntiKnockback.enabled) ci.cancel();
    }
}
