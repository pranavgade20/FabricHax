package io.github.pranavgade20.fabrichax;

import io.github.pranavgade20.fabrichax.clienthax.Criticals;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;


public class EntityClickManager implements AttackEntityCallback {
    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, Entity entity, EntityHitResult entityHitResult) {
        try {

        PlayerInventory inventory = playerEntity.getInventory();
        int slot = inventory.selectedSlot;
        float maxDamage = getDamage(playerEntity, inventory.getMainHandStack(), entity);
        for (int i = 0; i < 9; i++) {
            ItemStack weapon = inventory.getStack(i);

            float damage = getDamage(playerEntity, weapon, entity);
            if (maxDamage < damage) {
                maxDamage = damage;
                slot = i;
            }
        }

        int toScroll = 9 + (inventory.selectedSlot - slot);
        toScroll %= 9;
        for (int i = 0; i < toScroll; i++) {
            inventory.scrollInHotbar(1);
        }

        if (Criticals.INSTANCE.enabled) {
            Utils.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(playerEntity.getX(), playerEntity.getY() + 0.001, playerEntity.getZ(), false));
            Utils.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(playerEntity.getX(), playerEntity.getY() + 0.0001, playerEntity.getZ(), false));
        }
        } catch (Exception e) {
            Settings.player.sendMessage(Text.of(e.toString()), false);
        }
        return ActionResult.PASS;
    }

    public float getDamage(PlayerEntity player, ItemStack weapon, Entity target) {
        float f = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        float h;
        if (target instanceof LivingEntity) {
            h = EnchantmentHelper.getAttackDamage(weapon, ((LivingEntity) target).getGroup());
        } else {
            h = EnchantmentHelper.getAttackDamage(weapon, EntityGroup.DEFAULT);
        }

        if (f > 0.0F || h > 0.0F) {
            f += h;
        }
        return f;
    }
}
