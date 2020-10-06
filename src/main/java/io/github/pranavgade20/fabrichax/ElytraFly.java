package io.github.pranavgade20.fabrichax;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;

public class ElytraFly {
    public static boolean enabled = false;
    public static void toggle() {
        if (Settings.player == null) return;

        ItemStack itemStack = Settings.player.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem() != Items.ELYTRA || !ElytraItem.isUsable(itemStack)) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("get elytra dumbo"), Settings.player.getUuid());
            return;
        }

        if (enabled) {
            Settings.player.abilities.flying = Fly.enabled;
            Settings.player.abilities.allowFlying = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled ElytraHax"), Settings.player.getUuid());
            enabled = false;
        } else {
            ClientSidePacketRegistry.INSTANCE.sendToServer(new ClientCommandC2SPacket(Settings.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled ElytraHax"), Settings.player.getUuid());

            Settings.player.abilities.flying = true;
            Settings.player.abilities.allowFlying = true;
            Settings.player.setPos(Settings.player.getX(), Settings.player.getY() + 0.5, Settings.player.getZ());
            enabled = true;
        }
    }

    public static String getHelpMessage() {
        return "ElytraFly - Creative mode flight while wearing an elytra.\n" +
                "You will be able to use your elytra in a much more stable way without using rockets. " +
                "This is much more stable than using Fly";
    }
}
