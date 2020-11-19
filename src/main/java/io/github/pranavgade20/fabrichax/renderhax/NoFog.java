package io.github.pranavgade20.fabrichax.renderhax;

public class NoFog extends RenderBase {
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
