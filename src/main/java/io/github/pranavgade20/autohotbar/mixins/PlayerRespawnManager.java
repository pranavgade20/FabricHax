package io.github.pranavgade20.autohotbar.mixins;


import io.github.pranavgade20.autohotbar.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
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
        Settings.player = (PlayerEntity)Settings.world.getEntityById(Settings.player.getEntityId());
        Settings.world = Settings.player.world;

//        Settings.channel.pipeline().addFirst("injected", new MessageToMessageEncoder() {
//            @Override
//            protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
//                out.add(msg);
//                System.out.println(msg.getClass().getSimpleName());
//            }

//            @Override
//            protected void decode(ChannelHandlerContext ctx, Packet<?> packet, List<Object> out) throws Exception {
//                out.add(packet);
//                String name = packet.getClass().getSimpleName();
//                if (name.equals("ChunkDataS2CPacket") || name.equals("LightUpdateS2CPacket")) {}
//                else System.out.println(name);
//            }
//        });

    }
}
