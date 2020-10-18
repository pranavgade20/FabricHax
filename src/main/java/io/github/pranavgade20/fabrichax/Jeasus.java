package io.github.pranavgade20.fabrichax;

public class Jeasus extends Base{
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
