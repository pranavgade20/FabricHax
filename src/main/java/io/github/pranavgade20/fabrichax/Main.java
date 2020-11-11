package io.github.pranavgade20.fabrichax;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		AttackBlockCallback.EVENT.register(new BlockClickManager());
		AttackEntityCallback.EVENT.register(new EntityClickManager());

		new Settings(); // instantiating Settings so that static variables are initialized.
		Settings.loadToggles();

		System.out.println("Loaded FabricHax!");
	}
}
