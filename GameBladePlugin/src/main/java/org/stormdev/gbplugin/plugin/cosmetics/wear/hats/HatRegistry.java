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
		CosmeticManager.registerCosmetic(new StoneSlabHat());
		CosmeticManager.registerCosmetic(new QuartzSlabHat());
		CosmeticManager.registerCosmetic(new NetherSlabHat());
		CosmeticManager.registerCosmetic(new WoodSlabHat());
		CosmeticManager.registerCosmetic(new RedstoneTorchHat());
		CosmeticManager.registerCosmetic(new RailHat());
		CosmeticManager.registerCosmetic(new IceHat());
		CosmeticManager.registerCosmetic(new FireHat());
		CosmeticManager.registerCosmetic(new LavaHat());
		PlayerSkullHat.registerAll();
		ColouredClayHat.registerAll();
		ColouredGlassHat.registerAll();
		ColouredWoolHat.registerAll();
		SkullHats.registerAll();
	}
}
