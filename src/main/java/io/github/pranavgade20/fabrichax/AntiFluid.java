package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class AntiFluid {
    public static boolean enabled = false;

    public static int up = 3;
    public static int down = 2;
    public static int north = 2;
    public static int south = 2;
    public static int east = 2;
    public static int west = 2;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AntiFluid"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AntiFluid"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "AntiFluid - replace fluids around you with blocks\n" +
                "\nConfiguration information:\n" +
                " ~ config AntiFluid <direction> <size>\n" +
                " (to configure shape to be built)\n" +
                " where directions include 'up, down, north, south, east, west'\n" +
                " for example, use `config Builder up 2`\n" +
                " to set this to fill 2 blocks above you.";
    }

    public static void config(String params) {
        try {
            String direction = params.split(" ")[1].toLowerCase();
            int size = Integer.parseInt(params.split(" ")[2]);

            if (direction.equals("up")) up = size+1;
            else if (direction.equals("down")) down = size;
            else if (direction.equals("north")) north = size;
            else if (direction.equals("south")) south = size;
            else if (direction.equals("east")) east = size;
            else if (direction.equals("west")) west = size;
            else {
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help AntiFluid) for more information."), Settings.player.getUuid());
                return;
            }
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("~ config AntiFluid " + params), Settings.player.getUuid());
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help AntiFluid) for more information."), Settings.player.getUuid());
        }
    }
}
