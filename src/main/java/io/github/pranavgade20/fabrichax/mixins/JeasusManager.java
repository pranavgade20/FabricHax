package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Jeasus;
import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class JeasusManager {
    @Inject(at = @At("RETURN"), method = "tick(Z)V")
    private void tick(boolean slowDown, CallbackInfo ci) {
        if (!Jeasus.enabled) return;

        if (Settings.player.isTouchingWater()) {
            Settings.player.abilities.flying = true;
            Jeasus.toggledFly = false;

            MinecraftClient.getInstance().player.input.sneaking = false;
            MinecraftClient.getInstance().player.input.jumping = false;
        } else if (!Jeasus.toggledFly) {
            Settings.player.abilities.flying = false;
            Jeasus.toggledFly = true;
        }
    }
}
