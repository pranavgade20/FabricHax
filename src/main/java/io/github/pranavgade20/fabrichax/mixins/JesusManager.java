package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.clienthax.Jesus;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class JesusManager {
    @Inject(at = @At("RETURN"), method = "tick(ZF)V")
    private void tick(boolean slowDown, float f, CallbackInfo ci) {
        if (!Jesus.INSTANCE.enabled) return;

        //TODO get player bounding box and check all corners
        if (!Settings.world.getBlockState(new BlockPos(new Vec3i((int) Settings.player.getPos().x, (int) Settings.player.getPos().x, (int) Settings.player.getPos().x))).getFluidState().isEmpty() ||
                !Settings.world.getBlockState(new BlockPos((new Vec3i((int) Settings.player.getPos().x, (int) Settings.player.getPos().x, (int) Settings.player.getPos().x)).add(0, 1, 0))).getFluidState().isEmpty()) {
            Settings.player.getAbilities().flying = true;
            Jesus.flyLock = true;
        } else if (Jesus.flyLock) {
            Settings.player.getAbilities().flying = false;
            Jesus.flyLock = false;
        }
    }
}
