package io.github.pranavgade20.fabrichax;

import net.minecraft.text.Text;

public class Walker extends Base{
    public static double speed = 5;

    public static Walker INSTANCE;
    public Walker() {
        INSTANCE = this;
    }

    @Override
    public String getHelpMessage() {
        return "Walker - walk in the direction you are looking at.\n" +
                "This is most effective on the nether roof\n" +
                "Configuration information:\n" +
                " ~ config Walker speed <speed>\n" +
                " where <speed> is the speed you want to move with.\n";
    }

    @Override
    public String getToolTip() {
        return "Walk in the direction you are looking at.";
    }

    @Override
    public void config(String params) {
        try {
            String parameter = params.split(" ")[1].toLowerCase();
            if ("speed".startsWith(parameter)) {
                speed = Double.parseDouble(params.split(" ")[2]);
                Settings.player.sendMessage(Text.of("Speed updated to: " + speed), false);
            } else {
                Settings.player.sendMessage(Text.of("Unidentified parameter" + parameter), false);
            }
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help template) for more information."), false);
        }
    }
}
