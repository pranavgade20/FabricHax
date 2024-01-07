package io.github.pranavgade20.fabrichax.mixins;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class NoSprintManager {
//    @Inject(at = @At("HEAD"), method = "sendPacket", cancellable = true) //TODO fixme
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ClientCommandC2SPacket) {
            Mode mode = ((ClientCommandC2SPacket) packet).getMode();
            if (mode == Mode.START_SPRINTING ||
            mode == Mode.STOP_SPRINTING) {
                ci.cancel();
            }
        }
    }
}
