package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClientGame.class)
public class PlayerManager {
    @Inject(at = @At("RETURN"), method = "onStartGameSession()V")
    public void setPlayer(CallbackInfo ci) {
        Settings.player = MinecraftClient.getInstance().player;
        Settings.world = MinecraftClient.getInstance().world;
    }
}
