package io.github.pranavgade20.fabrichax.scripting;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TestScript extends ScriptBase {
    public static TestScript INSTANCE;
    public TestScript() {
        INSTANCE = this;
    }

    LinkedList<String> script = Arrays.stream(("function setup(player, world, interaction) {\n" +
            "player, world, interaction\n" +
            "}\n" +
            "\n" +
            "function tick(player, world, interaction) {\n" +
            "print(player.getPos().x);\n" +
            "}\n").split("\n")).collect(Collectors.toCollection(LinkedList::new));
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    @Override
    public boolean toggle() {
        if (!enabled) {
            try {
                engine.eval(script.stream().reduce((a, b) -> a+b).get());
                ((Invocable)(engine)).invokeFunction("setup", Settings.player, Settings.world);
            } catch (ScriptException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return super.toggle();
    }

    public void tick() throws ScriptException {
        if (!INSTANCE.enabled) return;
        try {
            ((Invocable)(engine)).invokeFunction("tick", Settings.player, Settings.world);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Screen getConfigScreen(Screen parent, String name) {
        return new ScriptScreen(Text.of(name), parent, INSTANCE);
    }

    @Override
    public LinkedList<String> getScript() {
        return script;
    }
}
