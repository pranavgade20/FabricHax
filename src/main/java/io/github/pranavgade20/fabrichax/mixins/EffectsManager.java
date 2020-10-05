package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Effects;
import io.github.pranavgade20.fabrichax.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientConnection.class)
public class EffectsManager {
    @Inject(at = @At("RETURN"), method = "channelActive")
    private void injectEffectPacket(ChannelHandlerContext channelHandlerContext, CallbackInfo info) {
        Settings.channel = channelHandlerContext.channel();

        Settings.channel.pipeline().addAfter("decoder", "injected-in", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext ctx, Packet<?> msg, List<Object> out) throws Exception {
                out.add(msg);
                while (!Effects.toInject.isEmpty()) {
                    out.add(Effects.toInject.remove(0));
                }
            }
        });
    }
}
