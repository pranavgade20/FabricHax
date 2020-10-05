package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class AntiFall {
    public static boolean enabled = false;

    public static Vec3d prevPos = null;
    public static boolean onGround;

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            prevPos = null;
            onGround = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AntiFall"), Settings.player.getUuid());
        } else {
            enabled = true;
            prevPos = Settings.player.getPos();
            onGround = Settings.player.isOnGround();
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AntiFall"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "AntiFall - You won't take fall damage.\n" +
                " Even if you fall from a very high place, you won't loose any heart.\n";
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
