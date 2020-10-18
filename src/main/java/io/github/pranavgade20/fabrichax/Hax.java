package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Hax<T extends Base> {
    private final T module;

    public Hax(T module) {
        this.module = module;
    }

    public static Hax<?> of(Class<? extends Base> klass) {
        try {
            return new Hax<>(klass.newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            return new Hax<>(new Base());
        }
    }

    public void toggle() {
        if (Settings.player == null) return;

        Settings.world = MinecraftClient.getInstance().world;
        Settings.player = MinecraftClient.getInstance().player;

        if (module.toggle()) {
            Settings.player.sendMessage(Text.of(((module.enabled ? "En" : "Dis") + "abled " + module.getClass().getSimpleName())), false);
        } else {
            Settings.player.sendMessage(Text.of("Couldn't toggle " + module.getClass().getSimpleName()), false);
        }
    }

    public String getHelpMessage() {
        return module.getHelpMessage();
    }

    public String getToolTip() {
        return module.getToolTip();
    }

    public void config(String params) {
        module.config(params);
    }

    public boolean isEnabled() {
        return module.enabled;
    }

    public String getModuleName() {
        return module.getClass().getSimpleName();
    }
}
