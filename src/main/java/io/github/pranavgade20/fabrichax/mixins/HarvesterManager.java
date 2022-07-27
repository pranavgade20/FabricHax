package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import io.github.pranavgade20.fabrichax.automationhax.Harvester;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class HarvesterManager {
    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (Harvester.count > 0) {
            Harvester.count--;
            return;
        }
        Harvester.count = 1; // once every two ticks
        try {
            if (!Settings.world.isChunkLoaded(new BlockPos(Settings.player.getX(), 0.0D, Settings.player.getZ()))) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (Harvester.INSTANCE.enabled) {
            for (int y = -Harvester.down; y <= Harvester.up; y++) {
                for (int x = -Harvester.west; x <= Harvester.east; x++) {
                    for (int z = -Harvester.north; z <= Harvester.south; z++) {
                        BlockPos blockPos = new BlockPos(Settings.player.getPos().add(x, y, z)); // gonna place on this

                        BlockHitResult hitResult = new BlockHitResult(
                                Settings.player.getPos(),
                                Direction.UP,
                                blockPos,
                                false
                        );

                        if (Harvester.isHarvestable(Settings.world.getBlockState(blockPos))) {
                            Utils.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
                            Utils.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
                            return;
                        }
                    }
                }
            }
        }
    }
}
