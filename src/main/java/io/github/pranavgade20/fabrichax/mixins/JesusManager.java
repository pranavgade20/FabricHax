package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.clienthax.Fly;
import io.github.pranavgade20.fabrichax.clienthax.Jesus;
import net.minecraft.block.Blocks;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class JesusManager {
    @Inject(at = @At("RETURN"), method = "tick(Z)V")
    private void tick(boolean slowDown, CallbackInfo ci) {
        if (!Jesus.INSTANCE.enabled) return;

        int nearbyWater = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (Settings.world.getBlockState(new BlockPos(Settings.player.getPos().add(x, 0, z))).isOf(Blocks.WATER)) {
                    nearbyWater++;
                }
            }
        }

        if (nearbyWater == 9) {
            Settings.player.abilities.flying = true;
            Jesus.flyLock = true;
        } else if (!Fly.INSTANCE.enabled) {
            Settings.player.abilities.flying = false;
            Jesus.flyLock = false;
        }
    }
}
