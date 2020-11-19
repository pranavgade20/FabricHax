package io.github.pranavgade20.fabrichax.automationhax;

public class Fastmine extends AutomationBase {
    public static Fastmine INSTANCE;
    public Fastmine() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "Fastmine - allows you to mine blocks 30% faster.\n" +
                "Just click on the block to mine once.";
    }

    @Override
    public String getToolTip() {
        return "Mine blocks 30% faster";
    }
}
