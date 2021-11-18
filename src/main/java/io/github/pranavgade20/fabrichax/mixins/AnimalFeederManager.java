package io.github.pranavgade20.fabrichax.mixins;

import io.github.pranavgade20.fabrichax.Settings;
import io.github.pranavgade20.fabrichax.Utils;
import io.github.pranavgade20.fabrichax.automationhax.AnimalFeeder;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.stream.Collectors;

@Mixin(ClientPlayerEntity.class)
public class AnimalFeederManager {
    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (AnimalFeeder.count > 0) {
            AnimalFeeder.count--;
            if (AnimalFeeder.count%2 == 1) return; // attempt to feed every 2 ticks
        } else {
            AnimalFeeder.count = 19; // attempt to calculate new entities once every 20 ticks

            Set<LivingEntity> entities = Settings.world.getNonSpectatingEntities(LivingEntity.class,
                    new Box(
                            Settings.player.getX()-AnimalFeeder.west,
                            Settings.player.getY()-AnimalFeeder.down,
                            Settings.player.getZ()-AnimalFeeder.north,
                            Settings.player.getX()+AnimalFeeder.east,
                            Settings.player.getY()+AnimalFeeder.up,
                            Settings.player.getZ()+AnimalFeeder.south
                    )
            ).stream().filter(e -> {
                if (e instanceof PassiveEntity || e instanceof ZoglinEntity) return !e.isBaby();
                return false;
            }).collect(Collectors.toSet());

            AnimalFeeder.toFeed.retainAll(entities);
            AnimalFeeder.surrounding.retainAll(entities);
            entities.removeAll(AnimalFeeder.surrounding);
            AnimalFeeder.toFeed.addAll(entities);
            AnimalFeeder.surrounding.addAll(entities);
            System.out.println(AnimalFeeder.toFeed);
        }
        try {
            if (!Settings.world.isChunkLoaded(new BlockPos(Settings.player.getX(), 0.0D, Settings.player.getZ()))) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (!AnimalFeeder.toFeed.isEmpty()) {
            LivingEntity e = AnimalFeeder.toFeed.iterator().next();
            AnimalFeeder.toFeed.remove(e);
            System.out.println(e);
            if (e instanceof AnimalEntity) {
                if (((AnimalEntity) e).isBreedingItem(Settings.player.getMainHandStack())) {
//                    Utils.sendPacket(PlayerInteractEntityC2SPacket.interactAt(e, false, Hand.MAIN_HAND, new Vec3d(0,0,0)));
                    Utils.sendPacket(PlayerInteractEntityC2SPacket.interact(e, false, Hand.MAIN_HAND));
                }
                else if (((AnimalEntity) e).isBreedingItem(Settings.player.getOffHandStack())) {
//                    Utils.sendPacket(PlayerInteractEntityC2SPacket.interactAt(e, false, Hand.OFF_HAND, new Vec3d(0,0,0)));
                    Utils.sendPacket(PlayerInteractEntityC2SPacket.interact(e, false, Hand.OFF_HAND));
                }
            }
        }
    }
}
