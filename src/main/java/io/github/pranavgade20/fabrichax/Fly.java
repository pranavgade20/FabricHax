package io.github.pranavgade20.fabrichax;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class Fly {
    public static boolean enabled = false;
    public static double count = 0d;
    static Timer noAfk = new Timer();
    public static void toggle() {
        if (Settings.player == null) return;

        if (enabled) {
            enabled = false;
            Settings.player.abilities.flying = ElytraFly.enabled;
            Settings.player.abilities.allowFlying = false;
            noAfk.cancel();

            count = 0;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Fly"), Settings.player.getUuid());
        } else {
            enabled = true;
            Settings.player.abilities.allowFlying = true;
            noAfk.scheduleAtFixedRate(new TimerTask() {
                double prevCount = 0;
                @Override
                public void run() {
                    if (Settings.player == null) return;
                    if (!Settings.player.abilities.flying) return;
                    if (prevCount == Fly.count) {
                        prevCount++;
                        //sending a packet so we arent just hanging there
                        ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerMoveC2SPacket.PositionOnly(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ(), Settings.player.isOnGround()));
                    } else {
                        prevCount = Fly.count;
                    }
                }
            }, 200, 200);
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Fly"), Settings.player.getUuid());
        }
    }

    public static String getHelpMessage() {
        return "Fly - Enables creative mode flight.\n" +
                "You will be able to fly without elytra, just double-tap jump key.\n" +
                "note: this isn't as stable as elytrafly, so you might be kicked depending on the server. Using elytrafly is recommended over using this.";
    }
}
