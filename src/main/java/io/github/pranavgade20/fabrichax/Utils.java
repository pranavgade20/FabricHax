package io.github.pranavgade20.fabrichax;

import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    
    public static HashMap<Integer, List<Vec3d>> positionsCache = new HashMap<>();
    public static List<Vec3d> getPositions(int up, int down, int east, int west, int north, int south) {
        Integer hash = up;
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
}
