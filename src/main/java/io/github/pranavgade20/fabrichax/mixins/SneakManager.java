package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.automationhax.AutoSneak;
import io.github.pranavgade20.fabrichax.automationhax.Scaffold;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(KeyboardInput.class)
public class SneakManager {
    @Inject(at = @At("RETURN"), method = "tick(Z)V")
    private void tick(boolean slowDown, CallbackInfo ci) {
        if (Settings.player == null || Settings.world == null) return;

        float f = 0.1f; // 0.2f / 2.0f (0.2f is less than 0.6f, the actual size of player)
        float height = 1.8f;
//        float jump_boost = Settings.player.getActiveStatusEffects().get(StatusEffects.JUMP_BOOST) == null ? 0 : Settings.player.getActiveStatusEffects().get(StatusEffects.JUMP_BOOST).getAmplifier()+1;
        float max_fall = 3.3f; //TODO account jump boost into calculations

        //TODO detect lava, magma, and other blocks that cause damage. also account for non air blocks without collision boxes like snow, string, grass, etc
        Vec3d pos = Settings.player.getPos();
        boolean flag = Settings.world.isSpaceEmpty(new Box(pos.getX() - (double)f, pos.getY()- max_fall, pos.getZ() - (double)f, pos.getX() + (double)f, pos.getY() + height, pos.getZ() + (double)f));
        if (AutoSneak.INSTANCE.enabled && flag && !Settings.player.abilities.flying) {
            Settings.player.input.sneaking = true;
        }
        if (Scaffold.INSTANCE.enabled && flag && !Settings.player.abilities.flying) {
            try {
                Hand hand;
                if (Objects.equals(Settings.player.getMainHandStack().getItem().getGroup(), ItemGroup.BUILDING_BLOCKS)) {
                    hand = Hand.MAIN_HAND;
                } else if (Objects.equals(Settings.player.getOffHandStack().getItem().getGroup(), ItemGroup.BUILDING_BLOCKS)) {
                    hand = Hand.OFF_HAND;
                } else return;

                Direction direction = Direction.DOWN;
                BlockPos blockPos = new BlockPos(Settings.player.getPos().subtract(0, 1, 0));
                if (!Settings.world.getBlockState(new BlockPos(Settings.player.getPos().add(0, -1, 1))).isAir())
                    direction = Direction.getFacing(0, 0, -1);
                if (!Settings.world.getBlockState(new BlockPos(Settings.player.getPos().add(0, -1, -1))).isAir())
                    direction = Direction.getFacing(0, 0, 1);
                if (!Settings.world.getBlockState(new BlockPos(Settings.player.getPos().add(1, -1, 0))).isAir())
                    direction = Direction.getFacing(-1, 0, 0);
                if (!Settings.world.getBlockState(new BlockPos(Settings.player.getPos().add(-1, -1, 0))).isAir())
                    direction = Direction.getFacing(1, 0, 0);
                BlockHitResult hitResult = new BlockHitResult(
                        Settings.player.getPos(),
                        direction,
                        blockPos.offset(direction, -1),
                        false
                );

                ActionResult res;
                if (hand == Hand.MAIN_HAND) res = Settings.player.getMainHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getMainHandStack(), hitResult));
                else res = Settings.player.getOffHandStack().useOnBlock(new ItemPlacementContext(Settings.player, hand, Settings.player.getOffHandStack(), hitResult));


                if (res == ActionResult.SUCCESS) {
                    ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerInteractBlockC2SPacket(hand, hitResult));
                    if (AutoSneak.INSTANCE.enabled)
                        Settings.player.input.sneaking = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
