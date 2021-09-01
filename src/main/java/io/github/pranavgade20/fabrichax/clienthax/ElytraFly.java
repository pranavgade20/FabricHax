package io.github.pranavgade20.fabrichax.clienthax;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;

public class ElytraFly extends ClientBase {
    public static ElytraFly INSTANCE;
    public ElytraFly() {
        INSTANCE = this;
    }

    @Override
    public boolean toggle() {
        ItemStack itemStack = Settings.player.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem() != Items.ELYTRA || !ElytraItem.isUsable(itemStack)) {
            Settings.player.sendMessage(Text.of("You need an elytra!"), false);
            return false;
        }

        if (enabled) {
            Settings.player.getAbilities().flying = false;
            Settings.player.getAbilities().allowFlying = Fly.INSTANCE.enabled;
        } else {
            Utils.sendPacket(new ClientCommandC2SPacket(Settings.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));

            Settings.player.getAbilities().flying = true;
            Settings.player.getAbilities().allowFlying = true;
        }
        return super.toggle();
    }

    @Override
    public String getHelpMessage() {
        return "ElytraFly - Creative mode flight while wearing an elytra.\n" +
                "You will be able to use your elytra in a much more stable way without using rockets. " +
                "This is much more stable than using Fly";
    }

    @Override
    public String getToolTip() {
        return "Creative mode flight while wearing an elytra.";
    }
}
