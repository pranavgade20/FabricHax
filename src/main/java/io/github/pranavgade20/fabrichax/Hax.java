package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class Hax {
    public static boolean enabled = false;

    public static void toggle() {
        MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Something went wrong. Please contact the developer."), Settings.player.getUuid());
    }

}
