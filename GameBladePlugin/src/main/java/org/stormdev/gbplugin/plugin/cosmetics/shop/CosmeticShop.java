package org.stormdev.gbplugin.plugin.cosmetics.shop;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CosmeticShop implements Listener {
	public CosmeticShop(){
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	
}
