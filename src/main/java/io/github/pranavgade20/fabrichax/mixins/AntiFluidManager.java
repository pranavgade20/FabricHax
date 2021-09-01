package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Utils;
import io.github.pranavgade20.fabrichax.automationhax.AntiFluid;
import io.github.pranavgade20.fabrichax.Settings;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayerEntity.class)
public class AntiFluidManager {
    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo ci) {
        try {
            if (!Settings.world.isChunkLoaded(new BlockPos(Settings.player.getX(), 0.0D, Settings.player.getZ()))) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (AntiFluid.INSTANCE.enabled) {
            Hand hand;
            if (Objects.equals(Settings.player.getMainHandStack().getItem().getGroup(), ItemGroup.BUILDING_BLOCKS)) {
                hand = Hand.MAIN_HAND;
            } else if (Objects.equals(Settings.player.getOffHandStack().getItem().getGroup(), ItemGroup.BUILDING_BLOCKS)) {
                hand = Hand.OFF_HAND;
            } else return;

            for (int y = -AntiFluid.down; y <= AntiFluid.up; y++) {
                for (int x = -AntiFluid.west; x <= AntiFluid.east; x++) {
                    for (int z = -AntiFluid.north; z <= AntiFluid.south; z++) {
                        BlockPos blockPos = new BlockPos(Settings.player.getPos().add(x, y, z));

                        BlockHitResult hitResult = new BlockHitResult(
                                Settings.player.getPos(),
                                Direction.UP,
                                blockPos,
                                false
                        );

                        Block target = Settings.player.clientWorld.getBlockState(blockPos).getBlock();
                        if (target.equals(Blocks.WATER) || target.equals(Blocks.LAVA)) {
                            ActionResult res;
                            if (hand == Hand.MAIN_HAND) res = Settings.player.getMainHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getMainHandStack(), hitResult));
                            else res = Settings.player.getOffHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getOffHandStack(), hitResult));

                            if (res == ActionResult.SUCCESS) {
                                Utils.sendPacket(new PlayerInteractBlockC2SPacket(hand, hitResult));
                            }
                        }
                    }
                }
            }
        }
    }
}
