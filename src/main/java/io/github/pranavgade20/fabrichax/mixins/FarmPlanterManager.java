package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.automationhax.FarmPlanter;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class FarmPlanterManager {
    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (FarmPlanter.count > 0) {
            FarmPlanter.count--;
            return;
        }
        FarmPlanter.count = 1; // once every two ticks
        try {
            if (!Settings.world.isChunkLoaded(new BlockPos(Settings.player.getX(), 0.0D, Settings.player.getZ()))) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (FarmPlanter.INSTANCE.enabled) {
            for (int y = -FarmPlanter.down; y <= FarmPlanter.up; y++) {
                for (int x = -FarmPlanter.west; x <= FarmPlanter.east; x++) {
                    for (int z = -FarmPlanter.north; z <= FarmPlanter.south; z++) {
                        BlockPos blockPos = new BlockPos(Settings.player.getPos().add(x, y, z)); // gonna place on this

                        BlockHitResult hitResult = new BlockHitResult(
                                Settings.player.getPos(),
                                Direction.UP,
                                blockPos,
                                false
                        );

                        if (!Settings.world.getBlockState(blockPos.up()).getBlock().equals(Blocks.AIR)) continue;
                        Hand hand;
                        if (FarmPlanter.isPlacable(Settings.world.getBlockState(blockPos).getBlock(), Settings.player.getMainHandStack().getItem())) hand = Hand.MAIN_HAND;
                        else if (FarmPlanter.isPlacable(Settings.world.getBlockState(blockPos).getBlock(), Settings.player.getOffHandStack().getItem())) hand = Hand.OFF_HAND;
                        else continue;

                        ActionResult res;
                        if (hand == Hand.MAIN_HAND) res = Settings.player.getMainHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getMainHandStack(), hitResult));
                        else res = Settings.player.getOffHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getOffHandStack(), hitResult));

                        if (res == ActionResult.SUCCESS) {
                            ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, hitResult));
                            return;
                        }
                    }
                }
            }
        }
    }
}
