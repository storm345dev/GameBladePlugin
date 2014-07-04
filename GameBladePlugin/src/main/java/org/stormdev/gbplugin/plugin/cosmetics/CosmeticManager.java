package org.stormdev.gbplugin.plugin.cosmetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbplugin.cosmetics.cosmetics.hats.HatRegistry;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.shop.CosmeticShop;

public class CosmeticManager {
	private CosmeticShop shop;
	private static Map<String, Cosmetic> cosmetics = new HashMap<String, Cosmetic>();
	
	public static void registerCosmetic(Cosmetic cosmetic){
		cosmetics.put(cosmetic.getID(), cosmetic);
	}
	
	public CosmeticManager(){
		shop = new CosmeticShop();
		GameBlade.logger.info("Loading cosmetics...");
		HatRegistry.load();
		GameBlade.logger.info("Cosmetics loaded!");
	}
	
	public void registerACosmetic(Cosmetic cosmetic){
		cosmetics.put(cosmetic.getID(), cosmetic);
	}
	
	public List<Cosmetic> getAllByType(CosmeticType type){
		List<Cosmetic> results = new ArrayList<Cosmetic>();
		for(Cosmetic c:cosmetics.values()){
			if(c.getType().equals(type)){
				results.add(c);
			}
		}
		return results;
	}
}
