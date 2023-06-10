package io.github.pranavgade20.fabrichax;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static HashMap<Integer, List<Vec3d>> positionsCache = new HashMap<>();
    public static PendingUpdateManager pendingUpdateManager;
    public static List<Vec3d> getPositions(int up, int down, int east, int west, int north, int south) {
        int hash = up;
        hash *= 40;
        hash += down;
        hash *= 40;
        hash += east;
        hash *= 40;
        hash += west;
        hash *= 40;
        hash += north;
        hash *= 40;
        hash += south;
        if (positionsCache.containsKey(hash)) return positionsCache.get(hash);
        List<Vec3d> ret = new LinkedList<>();
        for (int y = -down; y <= up; y++) {
            for (int x = -west; x <= east; x++) {
                for (int z = -north; z <= south; z++) {
                    ret.add(new Vec3d(x, y, z));
                }
            }
        }
        Vec3d origin = new Vec3d(0, 1, 0);
        ret.sort(Comparator.comparingDouble(a -> a.distanceTo(origin)));
        positionsCache.put(hash, ret);
        return positionsCache.get(hash);
    }

    public static void sendPacket(Packet<?> packet) {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
            return;
        }

        throw new IllegalStateException("Cannot send packets when not in game!");
    }


    public static void sendPacket(SequencedPacketCreator supplier) {
        pendingUpdateManager.incrementSequence();

        try {
            int i = pendingUpdateManager.getSequence();
            Packet<ServerPlayPacketListener> packet = supplier.predict(i);
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
        } catch (Throwable var7) {
            if (pendingUpdateManager != null) {
                try {
                    pendingUpdateManager.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }

            throw var7;
        }

        if (pendingUpdateManager != null) {
            pendingUpdateManager.close();
        }

    }
}
