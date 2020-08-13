package io.github.pranavgade20.autohotbar;

public class Fly {
    public static boolean enabled;
    public static void toggle() {
        if (Settings.player == null) return;
        Settings.player.abilities.allowFlying = !Settings.player.abilities.allowFlying;
        enabled = Settings.player.abilities.allowFlying;
        System.out.println("Toggled flying");
    }
}
