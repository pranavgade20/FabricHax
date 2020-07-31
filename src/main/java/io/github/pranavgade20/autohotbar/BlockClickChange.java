package io.github.pranavgade20.autohotbar;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BlockClickChange implements AttackBlockCallback {
    public HashMap<String, String> tools;
    public BlockClickChange() {
        tools = new HashMap<>();
        tools.put("shovel", "[\\S\\s]*dirt [\\S\\s]*powder podzol gravel grass[\\S\\s]* farmland snow[\\S\\s]* [\\S\\s]*sand[\\S\\s]*");
        tools.put("_axe", "[\\S\\s]*log [\\S\\s]*wood [\\S\\s]*planks [\\S\\s]*fence[\\S\\s]* [\\S\\s]*chest");
        tools.put("pickaxe", "[\\S\\s]*stone[\\S\\s]* [\\S\\s]*diorite[\\S\\s]* [\\S\\s]*andesite[\\S\\s]* [\\S\\s]*ore[\\S\\s]* [\\S\\s]*nylium[\\S\\s]* [\\S\\s]*terracotta[\\S\\s]* [\\S\\s]*quartz[\\S\\s]* [\\S\\s]*ice[\\S\\s]* [\\S\\s]*prismarine[\\S\\s]*  [\\S\\s]*shulker_box hopper  [\\S\\s]*piston[\\S\\s]* [\\S\\s]*glass[\\S\\s]*");
        tools.put("hoe", "[\\S\\s]*leaves[\\S\\s]* [\\S\\s]*wart[\\S\\s]*");
        tools.put("shears", "[\\S\\s]*leaves[\\S\\s]* [\\S\\s]*wool[\\S\\s]* [\\S\\s]*carpet[\\S\\s]*");
    }
    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction) {
        PlayerInventory inventory = playerEntity.inventory;
        int slot = -1;
        String blockName = Registry.BLOCK.getId(world.getBlockState(blockPos).getBlock()).getPath();
        inv_loop : for (int i = 0; i < 9; i++) {
            String itemName = Registry.ITEM.getId(inventory.getStack(i).getItem()).getPath();
            for (Map.Entry<String, String> entry : tools.entrySet()) {
                String tool = entry.getKey();
                String blocks = entry.getValue();
                for (String block : blocks.split(" ")) {
                    if (Pattern.matches(block, blockName.toLowerCase())) {
                        if (itemName.contains(tool)) {
                            if (inventory.getStack(i).getDamage() + 1 != inventory.getStack(i).getMaxDamage()) {
                                slot = i;
                                break inv_loop;
                            }
                        }
                    }
                }
            }
        }

        if (slot != -1) {
            int toScroll = 9 + (inventory.selectedSlot - slot);
            toScroll %= 9;
            for (int i = 0; i < toScroll; i++) {
                inventory.scrollInHotbar(1);
            }
        }

        return ActionResult.PASS;
    }

    public boolean isUsingEffectiveTool(ItemStack item, BlockState block) { // modified from net.minecraft.entity.player.PlayerEntity.class
        return !block.isToolRequired() || item.isEffectiveOn(block);
    }

}
