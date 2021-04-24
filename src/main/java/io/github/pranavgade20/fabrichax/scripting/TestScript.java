package io.github.pranavgade20.fabrichax.scripting;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.renderhax.FullBright;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestScript extends ScriptBase {
    public static TestScript INSTANCE;
    public TestScript() {
        INSTANCE = this;
    }

    static String script = "function tick(player, world, interaction) {" +
            "print(player.getPos().x);" +
            "}";
    static ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    @Override
    public boolean toggle() {
        if (!enabled) {
            try {
                engine.eval(script);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        return super.toggle();
    }

    public static void tick() throws ScriptException {
        if (!INSTANCE.enabled) return;
        try {
            ((Invocable)(engine)).invokeFunction("tick", Settings.player, Settings.world);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


}
