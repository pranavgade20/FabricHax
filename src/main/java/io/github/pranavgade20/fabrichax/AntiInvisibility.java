package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

public class AntiInvisibility {
    public static boolean enabled = false;
    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AntiInvisibility"), Settings.player.getUuid());
            enabled = false;
        } else {
            Settings.world.getEntities().forEach((entity) -> {
                if (entity instanceof LivingEntity) {
                    if (((LivingEntity)entity).hasStatusEffect(StatusEffects.INVISIBILITY)) {
                        ((LivingEntity)entity).removeStatusEffect(StatusEffects.INVISIBILITY);
                    }
                }
            });
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AntiInvisibility"), Settings.player.getUuid());
            enabled = true;
        }
    }

    public static String getHelpMessage() {
        return "AntiInvisibility - makes invisible players visible.\n" +
                "You will be able to see all the player who have used invisibility potion to become invisible.";
    }
}
