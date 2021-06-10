package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.clienthax.Effects;
import io.github.pranavgade20.fabrichax.clienthax.ElytraFly;
import io.github.pranavgade20.fabrichax.clienthax.Fly;
import io.github.pranavgade20.fabrichax.renderhax.FreeCam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayerManager {
    @Inject(at = @At("RETURN"), method = "onGameJoin")
    public void setPlayer(GameJoinS2CPacket p, CallbackInfo ci) {
        if (Settings.player != null) Settings.saveToggles();
        Settings.player = MinecraftClient.getInstance().player;
        Settings.world = MinecraftClient.getInstance().world;

        Settings.loadToggles();

        if (Effects.INSTANCE.enabled)
            Effects.cache.forEach((effect, instance) -> Settings.player.getActiveStatusEffects().put(effect, instance));

        if (Fly.INSTANCE.enabled || ElytraFly.INSTANCE.enabled)
            try {
                Settings.player.getAbilities().allowFlying = true;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        if (FreeCam.INSTANCE.enabled) {
            FreeCam.INSTANCE.enabled = false;
        }
    }

    @Inject(at = @At("RETURN"), method = "onDisconnect")
    public void setPlayer(DisconnectS2CPacket p, CallbackInfo ci) {
        Settings.player = null;
        Settings.world = null;
        Settings.saveToggles();
    }

    @Inject(at = @At("RETURN"), method = "onDisconnected")
    public void setPlayer(Text reason, CallbackInfo ci) {
        Settings.player = null;
        Settings.world = null;
        Settings.saveToggles();
    }

    @Inject(at = @At("RETURN"), method = "onPlayerRespawn")
    public void setPlayer(PlayerRespawnS2CPacket p, CallbackInfo ci) {
        if (Settings.player != null) Settings.saveToggles();
        Settings.player = MinecraftClient.getInstance().player;
        Settings.world = MinecraftClient.getInstance().world;

        Settings.loadToggles();

        if (Effects.INSTANCE.enabled)
            Effects.cache.forEach((effect, instance) -> Settings.player.getActiveStatusEffects().put(effect, instance));

        if (Fly.INSTANCE.enabled || ElytraFly.INSTANCE.enabled)
            try {
                Settings.player.getAbilities().allowFlying = true;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        if (FreeCam.INSTANCE.enabled) {
            FreeCam.INSTANCE.enabled = false;
        }
    }
}
