package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.clienthax.AntiFall;
import io.github.pranavgade20.fabrichax.clienthax.ElytraFly;
import io.github.pranavgade20.fabrichax.clienthax.Fly;
import net.minecraft.entity.EntityType;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientConnection.class)
public class ChannelManager {
    @ModifyVariable(at = @At("HEAD"), method = "sendInternal", ordinal = 0)
    public Packet<?> sendInternal(Packet<?> packet) {
        if (Fly.INSTANCE.enabled || ElytraFly.INSTANCE.enabled) {
            if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround && Settings.player.getAbilities().flying) {
                if (!Settings.player.isInsideWaterOrBubbleColumn() && Settings.world.isSpaceEmpty(EntityType.PLAYER.createSimpleBoundingBox(Settings.player.getX(), Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)), Settings.player.getZ())))
                    return (new PlayerMoveC2SPacket.PositionAndOnGround(
                            Settings.player.getX(),
                            Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)),
                            Settings.player.getZ(),
                            Settings.player.isOnGround()
                    ));
                else return (new PlayerMoveC2SPacket.PositionAndOnGround(
                        Settings.player.getX(),
                        Settings.player.getY(),
                        Settings.player.getZ(),
                        Settings.player.isOnGround()
                ));
            } else if (packet instanceof PlayerMoveC2SPacket.Full && Settings.player.getAbilities().flying) {
                if (Settings.world.isSpaceEmpty(EntityType.PLAYER.createSimpleBoundingBox(Settings.player.getX(), Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)), Settings.player.getZ())))
                    return (new PlayerMoveC2SPacket.Full(
                            Settings.player.getX(),
                            Settings.player.getY() + (1.25 * Math.pow(Math.sin(Fly.count++ / 20), 2)),
                            Settings.player.getZ(),
                            Settings.player.getYaw(),
                            Settings.player.getPitch(),
                            Settings.player.isOnGround()
                    ));
                else return (new PlayerMoveC2SPacket.Full(
                        Settings.player.getX(),
                        Settings.player.getY(),
                        Settings.player.getZ(),
                        Settings.player.getYaw(),
                        Settings.player.getPitch(),
                        Settings.player.isOnGround()
                ));
            }
        }
        if (AntiFall.INSTANCE.enabled) {
            if (packet instanceof PlayerMoveC2SPacket.Full || packet instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
                float testWidth = 0.3f;
                float height = 1.8f;
                float max_fall = (float) (Settings.player.getVelocity().y * -2); //TODO account jump boost into calculations
                Vec3d pos = Settings.player.getPos();
                boolean flag = Settings.world.isSpaceEmpty(new Box(pos.getX() - (double)testWidth, pos.getY()- max_fall, pos.getZ() - (double)testWidth, pos.getX() + (double)testWidth, pos.getY() + height, pos.getZ() + (double)testWidth));
                if (Settings.player.getVelocity().y < -0.7 && !flag) {
                    Settings.player.setVelocity(Settings.player.getVelocity().x, 0.3, Settings.player.getVelocity().z);
                    Settings.player.velocityDirty = true;
                AntiFall.prevPos = Settings.player.getPos();
                AntiFall.onGround = Settings.player.isOnGround();
                }
            }
        }
        return packet;
    }
//    @Inject(at = @At("HEAD"), method = "channelActive")
//    public void setChannel(ChannelHandlerContext context, CallbackInfo info) {
//        Channel channel = context.channel();
//
//        Settings.channel = channel;
//        if (channel instanceof LocalChannel) return;
//
//        channel.pipeline().addAfter("encoder", "injected-out", new MessageToMessageEncoder<Packet<?>>() {
//            @Override
//            protected void encode(ChannelHandlerContext ctx, Packet<?> packet, List<Object> out) {
//                boolean added = false;
//                if (!added) {
//                    out.add(packet);
//                }
//
//                if (packet instanceof PlayerMoveC2SPacket) return;
//                if (packet instanceof HandSwingC2SPacket) return;
//                if (packet instanceof KeepAliveC2SPacket) return;
//                if (packet instanceof PlayerActionC2SPacket) {
//                    System.out.println(((PlayerActionC2SPacket) packet).getAction());
//                    return;
//                }
//
//                System.out.println(packet.getClass().getName());
//            }
//        });
//
//        channel.pipeline().addAfter("decoder", "injected-in", new MessageToMessageDecoder<Packet<?>>() {
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
//    }
}
