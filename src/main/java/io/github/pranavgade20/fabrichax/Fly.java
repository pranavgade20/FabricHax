package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;

public class Fly {
    public static boolean enabled = false;
    public static double count = 0d;
    public static void toggle() {
        if (Settings.player == null) return;
        MinecraftClient.getInstance().player.abilities.flying = true;
        MinecraftClient.getInstance().player.abilities.allowFlying = true;
        enabled = Settings.player.abilities.allowFlying;
        System.out.println("Toggled flying");
    }
}
