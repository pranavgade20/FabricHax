package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import io.github.pranavgade20.fabrichax.clienthax.ElytraFly;
import io.github.pranavgade20.fabrichax.clienthax.Fly;
import io.github.pranavgade20.fabrichax.clienthax.Jesus;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class FlyManager {
    @Inject(at = @At("HEAD"), method = "sendAbilitiesUpdate", cancellable = true)
    private void suppressUpdateFlyPacket(CallbackInfo ci) {
        if (Fly.INSTANCE.enabled) ci.cancel();
        if (Jesus.INSTANCE.enabled && Jesus.flyLock) ci.cancel();
        if (ElytraFly.INSTANCE.enabled) {
            ci.cancel();
            ItemStack itemStack = Settings.player.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() != Items.ELYTRA || !ElytraItem.isUsable(itemStack)) {
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Could not start ElytraHax. Check your elytra."), Settings.player.getUuid());
                Settings.player.getAbilities().flying = false;
                return;
            }
            Utils.sendPacket(new ClientCommandC2SPacket(Settings.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }
    }
}
