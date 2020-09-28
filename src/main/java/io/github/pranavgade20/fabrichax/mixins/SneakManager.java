package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.AutoSneak;
import io.github.pranavgade20.fabrichax.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.xml.soap.SAAJResult;

@Mixin(KeyboardInput.class)
public class SneakManager {
    @Inject(at = @At("RETURN"), method = "tick(Z)V")
    private void tick(boolean slowDown, CallbackInfo ci) {
        boolean flag = true;
        for (int i = 1; i < 5; i++) { //TODO account jump boost into calculations
            //TODO detect lava, magma, and other blocks that cause damage. also account for non air blocks without collision boxes like snow, string, grass, etc
            if (!Settings.world.getBlockState(new BlockPos(Settings.player.getPos().subtract(0, i, 0))).isAir()) flag = false;
//            flag &= Settings.world.getBlockState(new BlockPos(Settings.player.getPos().subtract(0, i, 0))).isAir();
        }
        if (AutoSneak.enabled && flag && !Settings.player.abilities.flying) {
            MinecraftClient.getInstance().player.input.sneaking = true;
        }
    }
}
