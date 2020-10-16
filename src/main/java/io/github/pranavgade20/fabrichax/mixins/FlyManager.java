package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.ElytraFly;
import io.github.pranavgade20.fabrichax.Fly;
import io.github.pranavgade20.fabrichax.Settings;
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
    private void suppressUpdatePacket(CallbackInfo ci) {
        if (Fly.enabled) ci.cancel();
        if (ElytraFly.enabled) {
            ci.cancel();
            ItemStack itemStack = Settings.player.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() != Items.ELYTRA || !ElytraItem.isUsable(itemStack)) {
                MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, Text.of("Could not start ElytraHax. Check your elytra."), Settings.player.getUuid());
                return;
            }
            ClientSidePacketRegistry.INSTANCE.sendToServer(new ClientCommandC2SPacket(Settings.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }
    }
}
