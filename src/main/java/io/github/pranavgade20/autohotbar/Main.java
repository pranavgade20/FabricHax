package io.github.pranavgade20.autohotbar;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		AttackBlockCallback.EVENT.register(new BlockClickChange());
		AttackEntityCallback.EVENT.register(new EntityClickChange());

		System.out.println("Loaded AutoHotbar!");
	}
}
