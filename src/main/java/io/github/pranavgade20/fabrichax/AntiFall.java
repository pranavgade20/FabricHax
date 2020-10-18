package io.github.pranavgade20.fabrichax;

import net.minecraft.util.math.Vec3d;

public class AntiFall extends Base{
    public static Vec3d prevPos = null;
    public static Vec3d lastGround = null;
    public static boolean onGround;

    public static AntiFall INSTANCE;
    public AntiFall() {
        INSTANCE = this;
    }

    @Override
    public boolean toggle() {
        if (enabled) {
            prevPos = null;
            lastGround = null;
            onGround = false;
        } else {
            prevPos = Settings.player.getPos();
            onGround = Settings.player.isOnGround();
        }
        return super.toggle();
    }

    @Override
    public String getHelpMessage() {
        return "AntiFall - Disable fall damage.\n" +
                "You wont take fall damage.\n";
    }

    @Override
    public String getToolTip() {
        return "Disable fall damage";
    }
}
