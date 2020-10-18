package io.github.pranavgade20.fabrichax;

import io.github.pranavgade20.fabrichax.gui.MainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GUI extends Base{
    public static GUI INSTANCE;
    public GUI() {
        INSTANCE = this;
    }

    public boolean toggle() {
        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().openScreen(null);
        } else {
            enabled = true;
            MinecraftClient.getInstance().openScreen(new MainScreen(Text.of("FabricHax")));
        }
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "GUI - Helps you manage your modules effectively.";
    }

    @Override
    public String getToolTip() {
        return "Manage your modules";
    }
}
