package io.github.pranavgade20.fabrichax.clienthax;

import io.github.pranavgade20.fabrichax.Settings;

public class Jeasus extends ClientBase {
    public static Jeasus INSTANCE;
    public Jeasus() {
        INSTANCE = this;
    }

    @Override
    public boolean toggle() {
        if (enabled) {
            Settings.player.abilities.flying = false;
        }
        return super.toggle();
    }

    @Override
    public String getHelpMessage() {
        return "Jeasus - walk on/in water.\n" +
                "Run on water without sinking.\n" +
                "Improved movement underwater.\n";
    }

    @Override
    public String getToolTip() {
        return "Walk on/in water.";
    }
}
