package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.clienthax.Jesus;
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

        //TODO get player bounding box and check all corners
        if (!Settings.world.getBlockState(new BlockPos(Settings.player.getPos())).getFluidState().isEmpty() ||
                !Settings.world.getBlockState(new BlockPos(Settings.player.getPos().add(0, 1, 0))).getFluidState().isEmpty()) {
            Settings.player.getAbilities().flying = true;
            Jesus.flyLock = true;
        } else if (Jesus.flyLock) {
            Settings.player.getAbilities().flying = false;
            Jesus.flyLock = false;
        }
    }
}
