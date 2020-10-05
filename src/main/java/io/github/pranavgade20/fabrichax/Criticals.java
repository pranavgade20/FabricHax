package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class Criticals {
    public static boolean enabled = false;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Criticals"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Criticals"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "Criticals - the player you hit, receives more damage.\n" +
                "The player you hit, receives 25% more damage";
    }
}
