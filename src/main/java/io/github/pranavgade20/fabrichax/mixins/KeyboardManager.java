package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;


@Mixin(Keyboard.class)
public class KeyboardManager {
    private final int PRIMARY_KEY = 80;

    private static HashMap<String, Integer> toggles = new HashMap<>();

    private static HashSet<Integer> keys_pressed = new HashSet<>();

    @Inject(at = @At("HEAD"), method = "onKey(JIIII)V")
    private void handleKeyPress(long window, int key, int scancode, int i, int j, CallbackInfo info) {
        if (i == 0) keys_pressed.remove(key);

        if (i == 1) {
            keys_pressed.add(key);

            if (keys_pressed.contains(PRIMARY_KEY) && key != PRIMARY_KEY) {
                Settings.keyDown(key);
            }
        }
    }
}
