package io.github.pranavgade20.fabrichax.renderhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends RenderBase {

    public static FreeCam INSTANCE;
    public static boolean flying = false;
    public FreeCam() {
        INSTANCE = this;
    }

    public static PlayerEntity e; // Do not rely on this being null
    @Override
    public boolean toggle() {
        if (enabled) {
            Settings.player.abilities.flying = flying;
            Settings.player.copyPositionAndRotation(e);
            Settings.world.removeEntity(e.getEntityId());
        } else {
            e = new OtherClientPlayerEntity(Settings.world, Settings.player.getGameProfile());
            e.setGlowing(true);
            e.resetPosition(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ());
            e.refreshPositionAndAngles(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ(), Settings.player.yaw, Settings.player.pitch);
            Settings.world.addEntity(e.getEntityId(), e);

            Settings.player.setBoundingBox(new Box(Settings.player.getPos(), Settings.player.getPos()));
            flying = Settings.player.abilities.flying;
            Settings.player.abilities.flying = true;
        }

        return super.toggle();
    }
}