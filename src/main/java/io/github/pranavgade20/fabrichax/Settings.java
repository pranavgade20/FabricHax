package io.github.pranavgade20.fabrichax;

import io.github.pranavgade20.fabrichax.automationhax.*;
import io.github.pranavgade20.fabrichax.clienthax.*;
import io.github.pranavgade20.fabrichax.renderhax.*;
import io.github.pranavgade20.fabrichax.todohax.NoSprint;
import io.github.pranavgade20.fabrichax.todohax.TodoBase;
import io.netty.channel.Channel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Settings {
    public static HashMap<Integer, Hax<?>> toggles;
    public static List<Hax<?>> categories;
    public static ClientPlayerEntity player;
    public static ClientWorld world;
    public static Channel channel;
    public static MinecraftClient client;

    static File file = new File(MinecraftClient.getInstance().runDirectory, "mods");
    static File config = new File(file, "fabrichax.config");

    public Settings() {
    }

    public static void keyDown(int key) {
        if (toggles.containsKey(key)) {
            toggles.get(key).toggle();
        }
    }

    public static void saveToggles() {
        try {
            FileWriter writer = new FileWriter(config, false);
            toggles.values().forEach((module) -> {
                try {
                    // config format is classname - t/f - newline
                    writer.write(module.getModuleName() + " " + (module.isEnabled() ? "t" : "f") + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (Settings.player != null) player.sendMessage(Text.of("Error while saving toggles to file. Resetting Toggles."), true);
        }

    }

    public static void loadToggles() {
        if (!config.exists()) saveToggles();

        try {
            FileReader reader = new FileReader(config);
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNext()) {
                String[] data = scanner.nextLine().split(" ");
                if (data.length < 2) continue;
                toggles.values().forEach(module -> {
                    if (data[0].equalsIgnoreCase(module.getModuleName())) {
                        boolean enabled = data[1].charAt(0) == 't';
                        if (enabled ^ module.isEnabled()) module.toggle();
                    }
                });
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (Settings.player != null) player.sendMessage(Text.of("Error while loading toggles from file. Resetting Toggles."), true);
        }
    }

    static {
        toggles = new HashMap<>();
        // TODO: read a config file to load defaults
        toggles.put((int)'C', Hax.of(Criticals.class));
        toggles.put((int)'K', Hax.of(Fly.class));
        toggles.put((int)'I', Hax.of(Digger.class));
        toggles.put((int)'H', Hax.of(ElytraFly.class));
        toggles.put((int)'U', Hax.of(AntiInvisibility.class));
        toggles.put((int)'L', Hax.of(AutoSneak.class));
        toggles.put((int)'J', Hax.of(Jeasus.class));
        toggles.put((int)'B', Hax.of(Builder.class));
        toggles.put((int)'Y', Hax.of(AntiFall.class));
        toggles.put((int)'O', Hax.of(Scaffold.class));
        toggles.put((int)'W', Hax.of(Walker.class));
        toggles.put((int)'G', Hax.of(AntiFluid.class));
        toggles.put((int)'R', Hax.of(GUI.class));
        toggles.put(-1, Hax.of(AutoHotbar.class));
        toggles.put(-2, Hax.of(Fastmine.class));
        toggles.put(-3, Hax.of(NoSprint.class));
        toggles.put(-4, Hax.of(Effects.class));
        toggles.put(-5, Hax.of(FullBright.class));
        toggles.put(-6, Hax.of(BetterFluids.class));
        toggles.put(-7, Hax.of(NoFog.class));

        categories = new LinkedList<>();
        categories.add(Hax.of(AutomationBase.class));
        categories.add(Hax.of(RenderBase.class));
        categories.add(Hax.of(ClientBase.class));
        categories.add(Hax.of(TodoBase.class));
    }
}
