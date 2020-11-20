package io.github.pranavgade20.fabrichax.automationhax;

import net.minecraft.util.math.Vec3d;

public class AutoSneak extends AutomationBase {
    public static Vec3d prevPos = null;
    public static boolean prefSafe = false;

    public static AutoSneak INSTANCE;
    public AutoSneak() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "AutoSneak - crouch the player when about to fall and take damage.\n" +
                "Also allows you to speedbridge, just move back and keep placing blocks.";
    }

    @Override
    public String getToolTip() {
        return "Crouches the player when about to fall and take damage";
    }
}
