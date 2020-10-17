package io.github.pranavgade20.fabrichax;

public class AntiInvisibility extends Base {
    public static AntiInvisibility INSTANCE;
    public AntiInvisibility() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "AntiInvisibility - Makes all invisible entities visible.\n";
    }

    @Override
    String getToolTip() {
        return "Make all invisible entities visible.";
    }
}
