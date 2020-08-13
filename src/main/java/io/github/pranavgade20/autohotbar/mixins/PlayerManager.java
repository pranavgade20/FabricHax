package io.github.pranavgade20.autohotbar.mixins;


import io.github.pranavgade20.autohotbar.Settings;
import net.minecraft.client.ClientGameSession;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientGameSession.class)
public class PlayerManager {
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/world/ClientWorldLnet/minecraft/client/network/ClientPlayerEntityLnet/minecraft/client/network/ClientPlayNetworkHandler;)V")
    public void setClient(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, ClientPlayNetworkHandler clientPlayNetworkHandler, CallbackInfo info) {
        Settings.player = clientPlayerEntity;
        Settings.world = clientWorld;
    }
}
