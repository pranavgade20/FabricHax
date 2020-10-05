package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class AutoSneak {
    public static boolean enabled = false;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AutoSneak"), Settings.player.getUuid());
        } else {
            enabled = true;
            Settings.world = MinecraftClient.getInstance().world;
            Settings.player = MinecraftClient.getInstance().player;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AutoSneak"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "AutoSneak - this allows you to bridge quickly.\n" +
                "You will be able to make bridges quickly without falling.\n" +
                "(You don't need to crouch while bridgin)";
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
