package io.github.pranavgade20.fabrichax.mixins;


import io.github.pranavgade20.fabrichax.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayerRespawnManager {
    @Inject(at = @At("HEAD"), method = "onPlayerRespawn(Lnet/minecraft/network/packet/s2c/play/PlayerRespawnS2CPacket;)V")
    public void setPlayer(PlayerRespawnS2CPacket p, CallbackInfo info) {
        Settings.player = MinecraftClient.getInstance().player;
        Settings.world = MinecraftClient.getInstance().world;
    }
}
