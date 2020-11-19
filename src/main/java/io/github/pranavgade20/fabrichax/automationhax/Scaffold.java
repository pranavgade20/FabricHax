package io.github.pranavgade20.fabrichax.automationhax;

public class Scaffold extends AutomationBase {
    public static Scaffold INSTANCE;
    public Scaffold() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "Scaffold - place blocks below you when you are going to fall.\n" +
                "Turn on autosneak and have sufficient blocks in hand.";
    }
}
