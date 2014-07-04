package org.stormdev.gbplugin.plugin.cosmetics;

import java.util.HashMap;
import java.util.Map;

import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbplugin.plugin.cosmetics.shop.CosmeticShop;

public class CosmeticManager {
	private CosmeticShop shop;
	private static Map<String, Cosmetic> cosmetics = new HashMap<String, Cosmetic>();
	
	public static void registerCosmetic(Cosmetic cosmetic){
		System.out.println("Registered cosmetic: "+cosmetic.getID());
		cosmetics.put(cosmetic.getID(), cosmetic);
	}
	
	public CosmeticManager(){
		shop = new CosmeticShop();
	}
}
