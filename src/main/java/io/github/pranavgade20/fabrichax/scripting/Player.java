package io.github.pranavgade20.fabrichax.scripting;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.util.math.Vec3d;

public class Player {
    public Vec3d getPos() {
        return Settings.player.getPos();
    }
}
