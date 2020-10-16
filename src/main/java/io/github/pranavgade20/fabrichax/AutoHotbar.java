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
        return "AutoHotbar - the ultimate hotbar management solution.\n" +
                "This automatically scrolls your main hand so you are holding the right tool for the job. " +
                "For example, switching to pickaxe when mining stone and switching to sword when attacking a zombie. " +
                "p.s.: for this to work, the tools need to be on your hotbar.";
    }

    public static String getToolTip() {
        return "The ultimate hotbar management solution";
    }
}
