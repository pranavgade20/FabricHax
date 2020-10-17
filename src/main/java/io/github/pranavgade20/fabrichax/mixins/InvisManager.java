package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.AntiInvisibility;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class InvisManager {
    @Inject(at = @At("RETURN"), method = "isInvisible()Z")
    private boolean overrideInvis(CallbackInfoReturnable<Boolean> ci) {
        if (AntiInvisibility.INSTANCE.enabled) return false;
        return ci.getReturnValueZ();
    }
}
