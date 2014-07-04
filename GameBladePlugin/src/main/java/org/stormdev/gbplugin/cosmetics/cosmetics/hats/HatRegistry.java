package org.stormdev.gbplugin.cosmetics.cosmetics.hats;

import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class HatRegistry {
	public static void load(){
		CosmeticManager.registerCosmetic(new GlassHat());
		ColouredGlassHat.registerAll();
	}
}
