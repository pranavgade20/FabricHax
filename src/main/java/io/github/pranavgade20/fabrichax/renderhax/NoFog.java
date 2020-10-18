package io.github.pranavgade20.fabrichax.renderhax;

import io.github.pranavgade20.fabrichax.Base;

class NoFog extends Base {
    public static NoFog INSTANCE;
    public NoFog() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "Nofog - Disables fog effect, including under lava and water.";
    }

    @Override
    public String getToolTip() {
        return "Disables fog effect";
    }
}
