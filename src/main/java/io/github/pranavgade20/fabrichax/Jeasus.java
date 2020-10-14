package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class Jeasus {
    public static boolean enabled = false;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            Settings.player.abilities.flying = false;
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Jeasus"), Settings.player.getUuid());
        } else {
            Settings.world = MinecraftClient.getInstance().world;
            Settings.player = MinecraftClient.getInstance().player;
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Jeasus"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "Jeasus - walk on water.\n" +
                "You will be able to walk on water without sinking.\n";
    }

    public static void config(String params) {
        try {
            String parameter = params.split(" ")[1].toLowerCase();

            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("The parameter was: " + parameter), Settings.player.getUuid());
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help template) for more information."), Settings.player.getUuid());
        }
    }
}
