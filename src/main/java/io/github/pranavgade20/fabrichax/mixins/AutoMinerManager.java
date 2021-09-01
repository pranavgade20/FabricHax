package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import io.github.pranavgade20.fabrichax.automationhax.AutoMiner;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class AutoMinerManager {
    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (AutoMiner.count > 1) {
            AutoMiner.count--;
            return;
        }
        AutoMiner.count = 2; // once every two ticks
        try {
            if (!Settings.world.isChunkLoaded(new BlockPos(Settings.player.getX(), 0.0D, Settings.player.getZ()))) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (AutoMiner.INSTANCE.enabled) {
            for (Vec3d delta : Utils.getPositions(AutoMiner.up, AutoMiner.down, AutoMiner.east, AutoMiner.west, AutoMiner.north, AutoMiner.south)) {
                BlockPos blockPos = new BlockPos(Settings.player.getPos().add(delta)); // gonna place on this

                BlockState state = Settings.world.getBlockState(blockPos);
                if (Settings.player.getMainHandStack().getMiningSpeedMultiplier(state) != 1.0F) {
                    Utils.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
                    Utils.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
                    Settings.world.setBlockBreakingInfo(Settings.player.getId(), blockPos, 9);
                    AutoMiner.count = (int) Math.ceil(1/state.calcBlockBreakingDelta(Settings.player, Settings.world, blockPos));
                    return;
                }
            }
        }
    }
}
