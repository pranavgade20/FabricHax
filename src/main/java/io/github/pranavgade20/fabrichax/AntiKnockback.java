package io.github.pranavgade20.fabrichax;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class AntiKnockback {
    public static boolean enabled = false;
    private float resistance = 0;

    public static void toggle() {
        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AntiKnockback"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AntiKnockback"), Settings.player.getUuid());
        }
    }

}
