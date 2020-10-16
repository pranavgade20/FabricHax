package io.github.pranavgade20.fabrichax;

import io.github.pranavgade20.fabrichax.gui.MainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class GUI {
    public static boolean enabled = false;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().openScreen(null);
        } else {
            enabled = true;
            MinecraftClient.getInstance().openScreen(new MainScreen(Text.of("FabricHax")));
        }
    }

    public static String getHelpMessage() {
        return "Template - this is how all classes should look like.\n" +
                "\nConfiguration information:\n" +
                " ~ config Template <params>\n" +
                " (to configure this class using the config() method)\n" +
                " where params are parameters\n";
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
