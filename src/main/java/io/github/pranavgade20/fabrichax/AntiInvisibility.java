package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class AntiInvisibility {
    public static boolean enabled = false;
    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AntiInvisibility"), Settings.player.getUuid());
            enabled = false;
        } else {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AntiInvisibility"), Settings.player.getUuid());
            enabled = true;
        }
    }

    public static String getHelpMessage() {
        return "AntiInvisibility - removes invisibility effect from all entities.\n";
    }
}
