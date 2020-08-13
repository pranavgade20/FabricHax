package io.github.pranavgade20.autohotbar.mixins;

import io.github.pranavgade20.autohotbar.Fly;
import io.github.pranavgade20.autohotbar.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientConnection.class)
public class ChannelManager {
    @Inject(at = @At("RETURN"), method = "channelActive(Lio/netty/channel/ChannelHandlerContext;)V")
    public void setChannel(ChannelHandlerContext channelHandlerContext, CallbackInfo info) {
        Settings.channel = channelHandlerContext.channel();

        Settings.channel.pipeline().addAfter("encoder", "injected", new MessageToMessageEncoder<Packet<?>>() {
            @Override
            protected void encode(ChannelHandlerContext ctx, Packet<?> packet, List<Object> out) {

                if (packet instanceof UpdatePlayerAbilitiesC2SPacket && Fly.enabled) {
                    out.add(new PlayerMoveC2SPacket.PositionOnly(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ(), Settings.player.isOnGround()));
                } else {
                    out.add(packet);
                }

                if (packet instanceof PlayerMoveC2SPacket.PositionOnly) {
                    if (Math.random() > 0.5) {
                        PlayerMoveC2SPacket.PositionOnly p = (PlayerMoveC2SPacket.PositionOnly) packet;
                        out.add(new PlayerMoveC2SPacket.PositionOnly(
                                Settings.player.getX(),
                                Settings.player.getY() + 0.1,
                                Settings.player.getZ(),
                                p.isOnGround()
                        ));
                    } else out.add(packet);
                }
//                if (packet instanceof PlayerMoveC2SPacket) return;
//                if (packet instanceof HandSwingC2SPacket) return;
//                if (packet instanceof KeepAliveC2SPacket) return;

//                if (packet instanceof ClientCommandC2SPacket){
//                    System.out.println(((ClientCommandC2SPacket) packet).getMode() + " " + ((ClientCommandC2SPacket) packet).getMountJumpHeight());
//                    return;
//                }
//
//                System.out.println(packet.getClass().getName());
            }
        });

    }
}
