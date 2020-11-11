package io.github.pranavgade20.fabrichax.renderhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class FreeCam extends RenderBase {

    public static FreeCam INSTANCE;
    public FreeCam() {
        INSTANCE = this;
    }

    private static Vec3d position = null;

    public static PlayerEntity e;
    @Override
    public boolean toggle() {
        if (enabled) {
            MinecraftClient.getInstance().interactionManager.setGameMode(MinecraftClient.getInstance().interactionManager.getPreviousGameMode());
            Settings.player.resetPosition(position.x, position.y, position.z);
            Settings.player.refreshPositionAfterTeleport(position.x, position.y, position.z);
            position = null;
            Settings.world.removeEntity(e.getEntityId());
        } else {
            e = new OtherClientPlayerEntity(Settings.world, Settings.player.getGameProfile());
            e.setGlowing(true);
            e.resetPosition(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ());
            e.refreshPositionAndAngles(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ(), Settings.player.yaw, Settings.player.pitch);
            Settings.world.addEntity(e.getEntityId(), e);

            MinecraftClient.getInstance().interactionManager.setGameMode(GameMode.SPECTATOR);
            Settings.player.setBoundingBox(new Box(Settings.player.getPos(), Settings.player.getPos()));
            position = new Vec3d(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ());
        }

        return super.toggle();
    }
}