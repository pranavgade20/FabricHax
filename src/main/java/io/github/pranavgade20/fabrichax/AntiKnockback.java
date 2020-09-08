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

public class AntiKnockback extends Hax {
    public static boolean enabled = false;
    private float resistance = 0;

    public static void toggle() {
        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled AntiKnockback"), Settings.player.getUuid());
        } else {
            enabled = true;
            class CustomAttributeContainer extends AttributeContainer {
                public Map<EntityAttribute, EntityAttributeInstance> custom = new HashMap<>();
                public CustomAttributeContainer(AttributeContainer c) {
                    super(null);
//                    EntityAttribute nokb = new EntityAttribute("attribute.name.generic.knockback_resistance", 1.0);
//                    Settings.player.getAttributes()
//                    EntityAttributeInstance i = Settings.player.getAttributeInstance(nokb);
                }
            }
            Settings.player.getAttributes().setFrom(new CustomAttributeContainer(Settings.player.getAttributes()));
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled AntiKnockback"), Settings.player.getUuid());
        }
    }

}
