package io.github.pranavgade20.fabrichax;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Digger {
    public static boolean enabled = false;
    public static int up = 3;
    public static int down = 1;
    public static int left = 3;
    public static int right = 3;
    public static void toggle() {
        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Disabled Digger"), Settings.player.getUuid());
        } else {
            enabled = true;
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Enabled Digger"), Settings.player.getUuid());
        }
    }
    public static boolean dig(ItemStack itemStack, World world, BlockPos blockPos, Direction direction) {
        for (int i = -left; i <= right; i++) {
            for (int j = -down; j <= up; j++) {
                switch (direction) {
                    case UP:
                    case DOWN:
                        return false; //not implemented
                    case EAST:
                    case WEST:
                        BlockPos block = blockPos.add(0, j, i);
                        if (getBlockBreakingSpeed(itemStack, world.getBlockState(block)) >= 34f) {
                            ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, block, direction));
                            ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, block, direction));

                            world.removeBlock(block, false);
                        }
                        break;
                    case NORTH:
                    case SOUTH:
                        BlockPos blk = blockPos.add(i, j, 0);
                        if (getBlockBreakingSpeed(itemStack, world.getBlockState(blk)) >= 34f) {
                            ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blk, direction));
                            ClientSidePacketRegistry.INSTANCE.sendToServer(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blk, direction));

                            world.removeBlock(blk, false);
                        }
                        break;
                }
            }
        }
        return true;
    }

    public static float getBlockBreakingSpeed(ItemStack tool, BlockState block) { // adapted from net.minecraft.entity.player.PlayerEntity.class
        float f = tool.getMiningSpeedMultiplier(block);

        if (f > 1.0F) {
            int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, tool);

            if (efficiency > 0 && !tool.isEmpty()) {
                f += (float) (efficiency * efficiency + 1);
            }
        }

        return f;
    }
}
