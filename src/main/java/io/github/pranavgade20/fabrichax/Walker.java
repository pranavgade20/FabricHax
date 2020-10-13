package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class Walker {
    public static boolean enabled = false;
    public static double speed = 5;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Walker"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Walker"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "Walker - walk in the direction you are looking at.\n" +
                "\nConfiguration information:\n" +
                " ~ config Walker speed <speed>\n" +
                " where <speed> is the speed you want to move with.\n";
    }

    public static void config(String params) {
        try {
            String parameter = params.split(" ")[1].toLowerCase();
            if ("speed".startsWith(parameter)) {
                speed = Double.parseDouble(params.split(" ")[2]);
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Speed updated to: " + speed), Settings.player.getUuid());
            } else {
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Unidentified parameter" + parameter), Settings.player.getUuid());
            }
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help template) for more information."), Settings.player.getUuid());
        }
    }
}
