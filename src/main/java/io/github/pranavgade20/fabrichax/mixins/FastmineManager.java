package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.automationhax.Fastmine;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class FastmineManager {
    @Inject(at = @At("RETURN"), method = "calcBlockBreakingDelta", cancellable = true)
    public void calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (Fastmine.INSTANCE.enabled && cir.getReturnValueF() < 0.7f) {
            cir.setReturnValue(cir.getReturnValueF()/0.7f);
        }
    }
}
