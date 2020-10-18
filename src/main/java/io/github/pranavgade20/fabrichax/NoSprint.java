package io.github.pranavgade20.fabrichax;

public class NoSprint extends Base{
    public static NoSprint INSTANCE;
    public NoSprint() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "NoSprint - Reduce hunger usage when sprinting.";
    }

    @Override
    public String getToolTip() {
        return "Reduce hunger usage when sprinting.";
    }
}
