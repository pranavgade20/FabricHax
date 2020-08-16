package io.github.pranavgade20.fabrichax;

import net.minecraft.entity.LivingEntity;

public class AntiInvisiblity {
    public static boolean enabled = true;
    public static void toggle() {
        if (enabled) {

        } else {
            Settings.world.getEntities().forEach((entity) -> {
                if (entity instanceof LivingEntity) {
//                    if (((LivingEntity)entity).hasStatusEffect())
                }
            });
        }
    }
}
