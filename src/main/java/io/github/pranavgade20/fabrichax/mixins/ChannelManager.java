package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
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
                boolean added = false;
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
                    }
                    added = true;
                }
                if (AntiFall.enabled) {
                    if (packet instanceof PlayerMoveC2SPacket.Both || packet instanceof PlayerMoveC2SPacket.PositionOnly) {
                        if (!AntiFall.onGround && ((PlayerMoveC2SPacket) packet).isOnGround() && AntiFall.lastGround != null && Math.abs(AntiFall.lastGround.y-AntiFall.prevPos.y) > 3) {
                            out.add(new PlayerMoveC2SPacket.PositionOnly(
                                    AntiFall.prevPos.x,
                                    AntiFall.prevPos.y + (0.01),
                                    AntiFall.prevPos.z,
                                    false
                            ));
                            added = true;
                        }
                        AntiFall.prevPos = Settings.player.getPos();
                        AntiFall.onGround = Settings.player.isOnGround();
                        if (!AntiFall.onGround && AntiFall.lastGround == null) AntiFall.lastGround = AntiFall.prevPos;
                        if (AntiFall.onGround) AntiFall.lastGround = null;
                    }
                }
                if (NoSprint.enabled) {
                    if (packet instanceof ClientCommandC2SPacket && (((ClientCommandC2SPacket) packet).getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING || ((ClientCommandC2SPacket) packet).getMode() == ClientCommandC2SPacket.Mode.STOP_SPRINTING)) {
                        out.add(new PlayerMoveC2SPacket.PositionOnly(
                                Settings.player.getX(),
                                Settings.player.getY(),
                                Settings.player.getZ(),
                                Settings.player.isOnGround()
                        ));
                        added = true;
                        System.out.println("Suppresed");
                    }
                }
                if (!added) {
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
