package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public class Effects {
    public static boolean enabled = true;
    public static HashMap<StatusEffect, StatusEffectInstance> cache = new HashMap<>();

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            cache.forEach(((statusEffect, statusEffectInstance) -> {
                Settings.player.getActiveStatusEffects().remove(statusEffect);
            }));
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Template"), Settings.player.getUuid());
        } else {
            enabled = true;
            cache.forEach((effect, instance) -> {
                Settings.player.getActiveStatusEffects().put(effect, instance);
            });
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Template"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "Effects - control the status effects you have.\n" +
                "\nConfiguration information:\n" +
                " ~ config Effects <effect_name> <amplifier>\n" +
                " (select amplifier to 0 to remove the effect)\n" +
                "Currently supported effects are-speed, slowness, jump_boost, nausea, night_vision, blindness, levitation, slow_falling, conduit_power, dolphins_grace";
    }

    public static void config(String params) {
        try {
            String name = params.split(" ")[1].toLowerCase();
            StatusEffect requested = null;

            for (Map.Entry<RegistryKey<StatusEffect>, StatusEffect> e : Registry.STATUS_EFFECT.getEntries()) {
                if (e.getValue().getName().toString().toLowerCase().contains(name)) {
                    requested = e.getValue();
                    break;
                }
            }
            int amplifier = params.split(" ").length > 2 ? Integer.parseInt(params.split(" ")[2].toLowerCase())-1 : 0;

            StatusEffectInstance effectInstance = new StatusEffectInstance(requested, Integer.MAX_VALUE, amplifier);
            if (amplifier == -1) {
                Settings.player.getActiveStatusEffects().remove(requested);
                cache.remove(requested);
            } else {
                Settings.player.getActiveStatusEffects().put(requested, effectInstance);
                cache.put(requested, effectInstance);
            }

            if (requested == null) {
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid effect."), Settings.player.getUuid());
                return;
            }
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Gave you " + name), Settings.player.getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help template) for more information."), Settings.player.getUuid());
        }
    }
}
