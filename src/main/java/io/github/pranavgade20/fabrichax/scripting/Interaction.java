package io.github.pranavgade20.fabrichax.scripting;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.text.Text;

public class Interaction {
    public static Interaction INSTANCE;
    public Interaction() {
        INSTANCE = this;
    }

    static {
        new Interaction();
    }

    static void addChat(Object o) {
        if (Settings.player == null) return;
        Settings.player.sendMessage(Text.of(String.valueOf(o)), false);
    }
}
