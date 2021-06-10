package io.github.pranavgade20.fabrichax.mixins.render;

import io.github.pranavgade20.fabrichax.renderhax.BetterFluids;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public class BetterFluidsManager {
    @Inject(at = @At("RETURN"), method = "isSideCovered")
    private static boolean overrideIsSideCovered(BlockView blockView, Direction direction, float f, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (BetterFluids.INSTANCE.enabled) return false;
        return cir.getReturnValueZ();
    }
}
