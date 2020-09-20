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
        if (getBlockBreakingSpeed(itemStack, world.getBlockState(blockPos)) >= 34f) return true;
        else return false;
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

    public static String getHelpMessage() {
        return "Digger - mine large chunks of blocks quickly.\n" +
                "You must be able to insta-mine the blocks for best results\n" +
                "(for example, you need an efficiency 5 shovel to use Digger on sand)\n" +
                "\nConfiguration information:\n" +
                " ~ config Digger <direction> <size>\n" +
                " (to configure shape to be dug out)\n" +
                " where directions include 'up, down, left, right'\n" +
                " and size is a non negative number" +
                " for example, use `config Digger 2 left`\n" +
                " to set this to mine 2 blocks to the left of your selected block.\n" +
                " \n" +
                " note: if you are kicked, try selecting a lower number for dig size";
    }

    public static void config(String params) {
        try {
            String direction = params.split(" ")[1].toLowerCase();
            int size = Integer.parseInt(params.split(" ")[2]);

            if (direction.equals("up")) up = size;
            else if (direction.equals("down")) down = size;
            else if (direction.equals("left")) left = size;
            else if (direction.equals("right")) right = size;
            else MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help digger) for more information."), Settings.player.getUuid());
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Invalid use: refer to help(~ help digger) for more information."), Settings.player.getUuid());
        }
    }
}
