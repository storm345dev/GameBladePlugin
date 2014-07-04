package org.stormdev.gbplugin.plugin.cosmetics.shop;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class CosmeticShop implements Listener {
	private IconMenu cosmeticMenu;
	private IconMenu hatMenu = null;
	private CosmeticManager manager;
	
	public CosmeticShop(CosmeticManager manager){
		this.manager = manager;
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
		cosmeticMenu = new IconMenu("Choose Cosmetic Type", 9, new OptionClickEventHandler(){

			@Override
			public void onOptionClick(OptionClickEvent event) {
				event.setWillClose(false);
				event.setWillDestroy(false);
				int pos = event.getPosition();
				
				if(pos == 0){	event.setWillClose(true); openHatMenu(event.getPlayer());}
				return;
			}}, GameBlade.plugin);
		cosmeticMenu.setOption(0, new ItemStack(Material.PUMPKIN), ChatColor.GOLD+"Hats", ChatColor.RED+"Buy Hats");
		updateShops();
	}
	
	public void updateShops(){
		List<Cosmetic> hats = manager.getAllByType(CosmeticType.HAT);
		
	}
	
	public void openHatMenu(Player player){
		//TODO
	}
	
	
}
