package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.AntiFall;
import io.github.pranavgade20.fabrichax.ElytraFly;
import io.github.pranavgade20.fabrichax.Fly;
import io.github.pranavgade20.fabrichax.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
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

        Settings.channel.pipeline().addAfter("encoder", "injected-out", new MessageToMessageEncoder<Packet<?>>() {
            @Override
            protected void encode(ChannelHandlerContext ctx, Packet<?> packet, List<Object> out) {
                if (Fly.enabled || ElytraFly.enabled) {
                    if (packet instanceof PlayerMoveC2SPacket.PositionOnly && Settings.player.abilities.flying) {
                        out.add(new PlayerMoveC2SPacket.PositionOnly(
                                Settings.player.getX(),
                                Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)),
                                Settings.player.getZ(),
                                Settings.player.isOnGround()
                        ));
                    } else if (packet instanceof PlayerMoveC2SPacket.Both && Settings.player.abilities.flying) {
                        out.add(new PlayerMoveC2SPacket.Both(
                                Settings.player.getX(),
                                Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)),
                                Settings.player.getZ(),
                                Settings.player.yaw,
                                Settings.player.pitch,
                                Settings.player.isOnGround()
                        ));
                    } else {
                        out.add(packet);
                    }
                } else if (AntiFall.enabled) {
                    if (packet instanceof PlayerMoveC2SPacket.Both || packet instanceof PlayerMoveC2SPacket.PositionOnly) {
                        if (!AntiFall.onGround && ((PlayerMoveC2SPacket) packet).isOnGround()) {
                            out.add(new PlayerMoveC2SPacket.PositionOnly(
                                    AntiFall.prevPos.x,
                                    AntiFall.prevPos.y + (0.01),
                                    AntiFall.prevPos.z,
                                    false
                            ));
                        } else out.add(packet);
                        AntiFall.prevPos = Settings.player.getPos();
                        AntiFall.onGround = Settings.player.isOnGround();
                    } else out.add(packet);
                } else {
                    out.add(packet);
                }
//                if (packet instanceof PlayerMoveC2SPacket && FreeCam.enabled) {
//                    out.add(FreeCam.fakePacket);
//                }
//                if (packet instanceof PlayerMoveC2SPacket) return;
//                if (packet instanceof HandSwingC2SPacket) return;
//                if (packet instanceof KeepAliveC2SPacket) return;
//
//                if (packet instanceof ClientCommandC2SPacket){
//                    System.out.println(((ClientCommandC2SPacket) packet).getMode() + " " + ((ClientCommandC2SPacket) packet).getMountJumpHeight());
//                    return;
//                }
//
//                System.out.println(packet.getClass().getName());
            }
        });

//        Settings.channel.pipeline().addAfter("decoder", "injected-in", new MessageToMessageDecoder<Packet<?>>() {
//            @Override
//            protected void decode(ChannelHandlerContext ctx, Packet<?> packet, List<Object> out) {
//                out.add(packet);
//                if (packet instanceof WorldTimeUpdateS2CPacket) return;
//                if (packet instanceof KeepAliveS2CPacket) return;
//                if (packet instanceof LightUpdateS2CPacket) return;
//                if (packet instanceof ChunkDataS2CPacket) return;
//                if (packet instanceof ScreenHandlerSlotUpdateS2CPacket) return;
//                if (packet instanceof UnloadChunkS2CPacket) return;
//                if (packet instanceof EntitySetHeadYawS2CPacket) return;
//                if (packet instanceof EntityS2CPacket) return; //sus
//
//                if (packet instanceof EntityVelocityUpdateS2CPacket) System.out.println(((EntityVelocityUpdateS2CPacket)packet).getId());
//                System.out.println(packet.getClass().getName());
//            }
//        });

    }
}
