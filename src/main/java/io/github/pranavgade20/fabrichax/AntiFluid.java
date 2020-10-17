package io.github.pranavgade20.fabrichax;

import net.minecraft.text.Text;

public class AntiFluid extends Base {
    public static int up = 3;
    public static int down = 2;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static AntiFluid INSTANCE;
    public AntiFluid() {
        INSTANCE = this;
    }
    public String getHelpMessage() {
        return "AntiFluid - replace fluids around you with blocks\n" +
                "\nConfiguration information:\n" +
                " ~ config AntiFluid <direction> <size>\n" +
                " (to configure shape to be built)\n" +
                " where directions include 'up, down, north, south, east, west'\n" +
                " for example, use `config Builder up 2`\n" +
                " to set this to fill 2 blocks above you.";
    }

    @Override
    public void config(String params) {
        try {
            String direction = params.split(" ")[1].toLowerCase();
            int size = Integer.parseInt(params.split(" ")[2]);

            switch (direction) {
                case "up":
                    up = size + 1;
                    break;
                case "down":
                    down = size;
                    break;
                case "north":
                    north = size;
                    break;
                case "south":
                    south = size;
                    break;
                case "east":
                    east = size;
                    break;
                case "west":
                    west = size;
                    break;
                default:
                    Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AntiFluid) for more information."), false);
                    return;
            }
            Settings.player.sendMessage(Text.of("~ config AntiFluid " + params), false);
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help AntiFluid) for more information."), false);
        }
    }
}
