package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class AutoHotbar extends Hax {
    public static boolean enabled = false;

    public static void toggle() {
        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AutoHotbar"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AutoHotbar"), Settings.player.getUuid());
        }
    }

}
