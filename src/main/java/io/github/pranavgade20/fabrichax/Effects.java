package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.Map;

public class Effects {
    public static boolean enabled = true;
    public static ArrayList<EntityStatusEffectS2CPacket> toInject = new ArrayList<>();

    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Template"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Template"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "Effects - control the status effects you have.\n" +
                "\nConfiguration information:\n" +
                " ~ config Effects <name> <duration> <amplifier>\n" +
                " (select duration to 0 to remove the effect)\n" +
                "Currently supported effects are-speed, slowness, jump_boost, nausea, night_vision, blindness, levitation, slow_falling, conduit_power, dolphins_grace";
    }

    public static void config(String params) {
        try {
            String name = params.split(" ")[1].toLowerCase();
            StatusEffect requested = StatusEffects.NIGHT_VISION;

            for (Map.Entry<RegistryKey<StatusEffect>, StatusEffect> e : Registry.STATUS_EFFECT.getEntries()) {
                if (e.getValue().getName().toString().toLowerCase().contains(name)) {
                    requested = e.getValue();
                    break;
                }
            }
            int duration = Integer.parseInt(params.split(" ")[2].toLowerCase());
            int amplifier = params.split(" ").length > 3 ? Integer.parseInt(params.split(" ")[3].toLowerCase()) : 1;

            StatusEffectInstance effectInstance = new StatusEffectInstance(requested, duration, amplifier);
            toInject.add(new EntityStatusEffectS2CPacket(Settings.player.getEntityId(), effectInstance));

            if (requested == null) {
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid effect."), Settings.player.getUuid());
                return;
            }
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Gave you " + requested.getName()), Settings.player.getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help template) for more information."), Settings.player.getUuid());
        }
    }
}
