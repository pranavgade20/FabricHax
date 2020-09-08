package io.github.pranavgade20.fabrichax;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;

public class ElytraFly extends Hax {
    public static boolean enabled = false;
    public static void toggle() {
        boolean hasElytra = false;
        for (ItemStack item : Settings.player.getArmorItems()) {
            if (item.getItem().asItem().getName().toString().toLowerCase().contains("elytra")) hasElytra = true;
        }

        if (!hasElytra) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("get elytra dumbo"), Settings.player.getUuid());
            return;
        }

        if (enabled) {
            MinecraftClient.getInstance().player.abilities.flying = Fly.enabled;
            MinecraftClient.getInstance().player.abilities.allowFlying = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled ElytraHax"), Settings.player.getUuid());
            enabled = false;
        } else {
            ClientSidePacketRegistry.INSTANCE.sendToServer(new ClientCommandC2SPacket(Settings.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled ElytraHax"), Settings.player.getUuid());

            MinecraftClient.getInstance().player.abilities.flying = true;
            MinecraftClient.getInstance().player.abilities.allowFlying = true;
            Settings.player.setPos(Settings.player.getX(), Settings.player.getY() + 0.5, Settings.player.getZ());
            enabled = true;
        }
    }
}
