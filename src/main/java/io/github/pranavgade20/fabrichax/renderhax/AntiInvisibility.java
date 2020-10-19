package io.github.pranavgade20.fabrichax.renderhax;

public class AntiInvisibility extends RenderBase {
    public static AntiInvisibility INSTANCE;
    public AntiInvisibility() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "AntiInvisibility - Makes all invisible entities visible.\n";
    }

    @Override
    public String getToolTip() {
        return "Make all invisible entities visible.";
    }
}
