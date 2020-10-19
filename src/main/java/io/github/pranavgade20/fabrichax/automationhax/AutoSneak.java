package io.github.pranavgade20.fabrichax.automationhax;

import io.github.pranavgade20.fabrichax.Base;

public class AutoSneak extends AutomationBase {
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
