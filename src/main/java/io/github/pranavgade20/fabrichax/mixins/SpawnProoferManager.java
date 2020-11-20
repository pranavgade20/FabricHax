package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.automationhax.SpawnProofer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class SpawnProoferManager {
    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (SpawnProofer.count > 0) {
            SpawnProofer.count--;
            return;
        }
        SpawnProofer.count = 20;
        try {
            if (!Settings.world.isChunkLoaded(new BlockPos(Settings.player.getX(), 0.0D, Settings.player.getZ()))) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (SpawnProofer.INSTANCE.enabled) {
            Hand hand;

//            for (int y = 10; y <= 10; y++) {
//                for (int x = 0; x <= 0; x++) {
//                    for (int z = 0; z <= 0; z++) {
            for (int y = -SpawnProofer.down; y <= SpawnProofer.up; y++) {
                for (int x = -SpawnProofer.west; x <= SpawnProofer.east; x++) {
                    for (int z = -SpawnProofer.north; z <= SpawnProofer.south; z++) {
                        BlockPos blockPos = new BlockPos(Settings.player.getPos().add(x, y, z)); // gonna place on this

                        BlockHitResult hitResult = new BlockHitResult(
                                Settings.player.getPos(),
                                Direction.UP,
                                blockPos,
                                false
                        );

                        if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, Settings.world, blockPos.up(), EntityType.ZOMBIFIED_PIGLIN)
                        && Settings.world.isSpaceEmpty(EntityType.ZOMBIFIED_PIGLIN.createSimpleBoundingBox((double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 1, (double)blockPos.getZ() + 0.5D))) {
                            if (!Block.getBlockFromItem(Settings.player.getMainHandStack().getItem()).getDefaultState()
                                    .allowsSpawning(Settings.world, blockPos.up(), EntityType.ZOMBIFIED_PIGLIN)) {
                                hand = Hand.MAIN_HAND;
                            } else if (!Block.getBlockFromItem(Settings.player.getOffHandStack().getItem()).getDefaultState()
                                    .allowsSpawning(Settings.world, blockPos.up(), EntityType.ZOMBIFIED_PIGLIN)) {
                                hand = Hand.OFF_HAND;
                            } else continue;

                            ActionResult res;
                            if (hand == Hand.MAIN_HAND) res = Settings.player.getMainHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getMainHandStack(), hitResult));
                            else res = Settings.player.getOffHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getOffHandStack(), hitResult));

                            if (res == ActionResult.SUCCESS) {
                                ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, hitResult));
                            }
                        }
                    }
                }
            }
        }
    }
}
