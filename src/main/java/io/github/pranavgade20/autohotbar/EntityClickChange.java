package io.github.pranavgade20.autohotbar;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class EntityClickChange implements AttackEntityCallback {
    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, Entity entity, EntityHitResult entityHitResult) {
        PlayerInventory inventory = playerEntity.inventory;
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            String itemName = Registry.ITEM.getId(inventory.getStack(i).getItem()).getPath();

            if (itemName.contains("sword")) slot = i;
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
}
