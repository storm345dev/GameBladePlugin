package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class HatRegistry {
	public static void load(){
		CosmeticManager.registerCosmetic(new GlassHat());
		CosmeticManager.registerCosmetic(new CopHat());
		CosmeticManager.registerCosmetic(new RainbowHat());
		CosmeticManager.registerCosmetic(new PumpkinHat());
		CosmeticManager.registerCosmetic(new GlowstoneHat());
		CosmeticManager.registerCosmetic(new BeaconHat());
		ColouredGlassHat.registerAll();
		ColouredWoolHat.registerAll();
	}
}
