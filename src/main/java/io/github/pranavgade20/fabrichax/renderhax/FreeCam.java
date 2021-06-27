package io.github.pranavgade20.fabrichax.renderhax;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends RenderBase {

    public static FreeCam INSTANCE;
    public static boolean flying = false;
    public FreeCam() {
        INSTANCE = this;
    }

    public static Entity e; // Do not rely on this being null
    @Override
    public boolean toggle() {
        if (enabled) {
            Settings.player.getAbilities().flying = flying;
            Settings.player.copyPositionAndRotation(e);
            Settings.world.removeEntity(e.getId(), Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        } else {
            e = new ZombieEntity(Settings.world);
            e.updatePositionAndAngles(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ(), Settings.player.getYaw(), Settings.player.getPitch());
            Settings.world.addEntity(e.getId(), e);
//            e = new OtherClientPlayerEntity(Settings.world, Settings.player.getGameProfile());
//            e.setGlowing(true);
//            e.updatePositionAndAngles(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ(), Settings.player.getYaw(), Settings.player.getPitch());
//            e.resetPosition();
//            Settings.world.addEntity(e.getId(), e);

            Settings.player.setBoundingBox(new Box(Settings.player.getPos(), Settings.player.getPos()));
            flying = Settings.player.getAbilities().flying;
            Settings.player.getAbilities().flying = true;
        }

        return super.toggle();
    }
}