package io.github.pranavgade20.fabrichax.clienthax;

import io.github.pranavgade20.fabrichax.Base;

public class Criticals extends ClientBase {
    public static Criticals INSTANCE;
    public Criticals() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "Criticals - All your attacks are critical hits.\n" +
                "The entity you hit receives more damage per hit.";
    }

    @Override
    public String getToolTip() {
        return "All your attacks are critical hits";
    }
}
