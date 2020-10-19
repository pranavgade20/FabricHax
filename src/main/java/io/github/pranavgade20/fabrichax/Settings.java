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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Settings {
    public static HashMap<Integer, Hax<?>> toggles;
    public static List<Hax<?>> categories;
    public static ClientPlayerEntity player;
    public static ClientWorld world;
    public static Channel channel;
    public static MinecraftClient client;

    public Settings() {
    }

    public static void keyDown(int key) {
        if (toggles.containsKey(key)) {
            toggles.get(key).toggle();
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
