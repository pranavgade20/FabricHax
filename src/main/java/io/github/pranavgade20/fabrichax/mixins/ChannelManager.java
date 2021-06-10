package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.clienthax.AntiFall;
import io.github.pranavgade20.fabrichax.clienthax.ElytraFly;
import io.github.pranavgade20.fabrichax.clienthax.Fly;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.local.LocalChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.entity.EntityType;
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
        if (channelHandlerContext.channel() instanceof LocalChannel) return;
        Settings.channel = channelHandlerContext.channel();

        Settings.channel.pipeline().addAfter("encoder", "injected-out", new MessageToMessageEncoder<Packet<?>>() {
            @Override
            protected void encode(ChannelHandlerContext ctx, Packet<?> packet, List<Object> out) {
                boolean added = false;
                if (Fly.INSTANCE.enabled || ElytraFly.INSTANCE.enabled) {
                    if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround && Settings.player.getAbilities().flying) {
                        if (Settings.world.isSpaceEmpty(EntityType.PLAYER.createSimpleBoundingBox(Settings.player.getX(), Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)), Settings.player.getZ())))
                            out.add(new PlayerMoveC2SPacket.PositionAndOnGround(
                                Settings.player.getX(),
                                Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)),
                                Settings.player.getZ(),
                                Settings.player.isOnGround()
                            ));
                        else out.add(new PlayerMoveC2SPacket.PositionAndOnGround(
                                Settings.player.getX(),
                                Settings.player.getY(),
                                Settings.player.getZ(),
                                Settings.player.isOnGround()
                        ));

                        added = true;
                    } else if (packet instanceof PlayerMoveC2SPacket.Full && Settings.player.getAbilities().flying) {
                        if (Settings.world.isSpaceEmpty(EntityType.PLAYER.createSimpleBoundingBox(Settings.player.getX(), Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)), Settings.player.getZ())))
                            out.add(new PlayerMoveC2SPacket.Full(
                                Settings.player.getX(),
                                Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)),
                                Settings.player.getZ(),
                                Settings.player.getYaw(),
                                Settings.player.getPitch(),
                                Settings.player.isOnGround()
                        ));
                        else out.add(new PlayerMoveC2SPacket.Full(
                                Settings.player.getX(),
                                Settings.player.getY(),
                                Settings.player.getZ(),
                                Settings.player.getYaw(),
                                Settings.player.getPitch(),
                                Settings.player.isOnGround()
                        ));

                        added = true;
                    }
                }
                if (AntiFall.INSTANCE.enabled) {
                    if (packet instanceof PlayerMoveC2SPacket.Full || packet instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                        if (!AntiFall.onGround && ((PlayerMoveC2SPacket) packet).isOnGround() && AntiFall.lastGround != null && Math.abs(AntiFall.lastGround.y-AntiFall.prevPos.y) > 3) {
                            out.add(new PlayerMoveC2SPacket.PositionAndOnGround(
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
                if (!added) {
                    out.add(packet);
                }

//                if (packet instanceof PlayerMoveC2SPacket) return;
//                if (packet instanceof HandSwingC2SPacket) return;
//                if (packet instanceof KeepAliveC2SPacket) return;
//                if (packet instanceof PlayerActionC2SPacket) {
//                    System.out.println(((PlayerActionC2SPacket) packet).getAction());
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
//                if (packet instanceof EntityVelocityUpdateS2CPacket) return;
//                if (packet instanceof EntityPositionS2CPacket) return;
//                if (packet instanceof BlockUpdateS2CPacket) return;
//                if (packet instanceof ChunkDeltaUpdateS2CPacket) return;
//
//                if (packet instanceof EntityTrackerUpdateS2CPacket) {
//                    List<DataTracker.Entry<?>> l = ((EntityTrackerUpdateS2CPacket) packet).getTrackedValues();
//                    Settings.debugHelper(l.get(0).getData());
//                }
//
//                if (packet instanceof PlayerAbilitiesS2CPacket) {
//                    System.out.println(((PlayerAbilitiesS2CPacket) packet).isInvulnerable() + ":");
//                }
//
//                System.out.println(packet.getClass().getName());
//            }
//        });
    }
}
