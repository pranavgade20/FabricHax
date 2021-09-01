package io.github.pranavgade20.fabrichax.clienthax;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Timer;
import java.util.TimerTask;

public class Fly extends ClientBase {
    public static double count = 0d;
    static Timer noAfk = new Timer();

    public static Fly INSTANCE;
    public Fly() {
        INSTANCE = this;
    }

    @Override
    public boolean toggle() {
        if (enabled) {
            Settings.player.getAbilities().flying = ElytraFly.INSTANCE.enabled;
            Settings.player.getAbilities().allowFlying = false;
            noAfk.cancel();

            count = 0;
        } else {
            Settings.player.getAbilities().allowFlying = true;
            noAfk = new Timer();
            noAfk.scheduleAtFixedRate(new TimerTask() {
                double prevCount = 0;
                @Override
                public void run() {
                    if (Settings.player == null) {
                        this.cancel();
                        return;
                    }
                    if (!Settings.player.getAbilities().flying) return;
                    if (prevCount == Fly.count) {
                        prevCount++;
                        //sending a packet so we arent just hanging there
                        Utils.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Settings.player.getX(), Settings.player.getY(), Settings.player.getZ(), Settings.player.isOnGround()));
                    } else {
                        prevCount = Fly.count;
                    }
                }
            }, 200, 200);
        }
        return super.toggle();
    }

    @Override
    public String getHelpMessage() {
        return "Fly - Enables creative mode flight.\n" +
                "You will be able to fly without elytra, just double-tap jump key.\n" +
                "note: this isn't as stable as elytrafly, so you might be kicked depending on the server. Using elytrafly is recommended over using this.";
    }
}
