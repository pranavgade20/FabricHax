package io.github.pranavgade20.fabrichax.mixins.scripting;

import io.github.pranavgade20.fabrichax.scripting.TestScript;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.script.ScriptException;

@Mixin(ClientPlayerEntity.class)
public class PlayerTick {
    @Inject(at = @At("HEAD"), method = "tick")
    void tick(CallbackInfo ci) {
        try {
            TestScript.INSTANCE.tick();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
