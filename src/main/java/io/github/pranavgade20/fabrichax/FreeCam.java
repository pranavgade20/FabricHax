package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class FreeCam {
    public static boolean enabled = false;
    public static PlayerMoveC2SPacket.PositionOnly fakePacket = null;
    private static Vec3d position = null;
    public static void toggle() {
        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().player.abilities.allowFlying = Fly.enabled;
            MinecraftClient.getInstance().player.abilities.flying = false;
            MinecraftClient.getInstance().interactionManager.setGameMode(GameMode.SURVIVAL);
            fakePacket = null;
            Settings.player.resetPosition(position.x, position.y, position.z);
            position = null;
        } else {
            enabled = true;
            MinecraftClient.getInstance().player.abilities.allowFlying = true;
            MinecraftClient.getInstance().player.abilities.flying = true;
            MinecraftClient.getInstance().interactionManager.setGameMode(GameMode.SPECTATOR);
            fakePacket = new PlayerMoveC2SPacket.PositionOnly(
                    Settings.player.getX(),
                    Settings.player.getY(),
                    Settings.player.getZ(),
                    Settings.player.isOnGround()
            );
            position = new Vec3d(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ());
            System.out.println("Enabled FreeCam");
        }
    }
}
