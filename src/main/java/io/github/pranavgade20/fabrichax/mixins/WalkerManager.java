package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Walker;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class WalkerManager {
    @Inject(at = @At("HEAD"), method = "tickMovement")
    public void tickMovement(CallbackInfo ci) {
        if (Walker.INSTANCE.enabled && !Settings.player.input.sneaking) {
            Vec3d newpos = Settings.player.getPos().add((new Vec3d(-Math.sin(Settings.player.yaw*(Math.PI/180.0d)), 0, Math.cos(Settings.player.yaw*(Math.PI/180.0d)))).multiply(Walker.speed/20.0d));
            if (Settings.world.getBlockState(new BlockPos(newpos)).isAir()) Settings.player.updatePosition(newpos.x, newpos.y, newpos.z);
        }
    }
}
