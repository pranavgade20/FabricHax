package io.github.pranavgade20.autohotbar;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class ElytraFly {
    public static boolean enabled = false;
    public static void toggle() {
        boolean hasElytra = false;
        for (ItemStack item : Settings.player.getArmorItems()) {
            if (item.getItem().asItem().getName().toString().toLowerCase().contains("elytra")) hasElytra = true;
        }

        if (!hasElytra) {
            System.out.println("get elytra dumbo");
            return;
        }

        ClientSidePacketRegistry.INSTANCE.sendToServer(new ClientCommandC2SPacket(Settings.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        System.out.println("Toggled elytra");

//        Settings.player.abilities.flying = true;
//        Settings.player.abilities.allowFlying = true;
//        Settings.player.setPos(Settings.player.getX(), Settings.player.getY() + 1.5, Settings.player.getZ());
        enabled = true;
    }
}
