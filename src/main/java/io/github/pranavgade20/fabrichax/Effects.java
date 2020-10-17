package io.github.pranavgade20.fabrichax;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public class Effects extends Base{
    public static HashMap<StatusEffect, StatusEffectInstance> cache = new HashMap<>();

    public static Effects INSTANCE;
    public Effects() {
        INSTANCE = this;
        enabled = true; // enabled by default
    }

    @Override
    public boolean toggle() {
        if (enabled) {
            cache.forEach(((statusEffect, statusEffectInstance) -> {
                Settings.player.getActiveStatusEffects().remove(statusEffect);
            }));
        } else {
            cache.forEach((effect, instance) -> {
                Settings.player.getActiveStatusEffects().put(effect, instance);
            });
        }
        return super.toggle();
    }

    @Override
    public String getHelpMessage() {
        return "Effects - control the status effects you have.\n" +
                "\nConfiguration information:\n" +
                " ~ config Effects <effect_name> <amplifier>\n" +
                " (select amplifier to 0 to remove the effect)\n" +
                "Currently supported effects are-speed, slowness, jump_boost, nausea, night_vision, blindness, levitation, slow_falling, conduit_power, dolphins_grace";
    }

    @Override
    public void config(String params) {
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
                Settings.player.sendMessage(Text.of("Invalid effect."), false);
                return;
            }
            Settings.player.sendMessage(Text.of("Gave you " + name), false);
        } catch (Exception e) {
            e.printStackTrace();
            Settings.player.sendMessage(Text.of("Invalid use: refer to help(~ help template) for more information."), false);
        }
    }
}
