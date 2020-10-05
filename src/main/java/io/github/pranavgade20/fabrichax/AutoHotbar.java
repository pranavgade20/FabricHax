package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class AutoHotbar {
    public static boolean enabled = true;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AutoHotbar"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AutoHotbar"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "AutoHotbar - switches to the right tool from your hotbar.\n" +
                "You won't need to switche between tools everytime, it automatically to the right tool from your hotbar according to your needs\n" +
                "For example, if you want to break grassblock I'll switch to a shovel and then if you want to break stone it'll switch to a pickaxe\n" +
                "note: The tools should be in your hotbar";
    }

}
