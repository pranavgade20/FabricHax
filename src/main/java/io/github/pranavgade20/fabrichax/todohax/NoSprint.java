package io.github.pranavgade20.fabrichax.todohax;

public class NoSprint extends TodoBase {
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
