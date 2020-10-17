package io.github.pranavgade20.fabrichax;

import net.minecraft.text.Text;

public class Base {
    public boolean enabled = false;

    boolean toggle() {
        enabled = !enabled;
        return true;
    }

    String getHelpMessage() {
        // this either means that the subclass does not override this method, or something happened in Hax.of()
        return "Uh oh - this should not be happening. Please send me a bug report if you can :)";
    }

    String getToolTip() {
        // this either means that the subclass does not override this method, or something happened in Hax.of()
        return "Uh oh - this should not be happening. Please send me a bug report if you can :)";
    }

    void config(String params) {
        Settings.player.sendMessage(Text.of("You cannot configure this module."),false);
    }
}
