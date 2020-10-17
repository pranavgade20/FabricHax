package io.github.pranavgade20.fabrichax;

public class AutoSneak extends Base{
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
    String getToolTip() {
        return "Crouches the player when about to fall and take damage";
    }
}
