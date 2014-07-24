package org.stormdev.gbplugin.plugin.cosmetics.carts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.Dye;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class BuyVehicleCosmeticsMenu implements OptionClickEventHandler {
	private static BuyVehicleCosmeticsMenu instance = null;
	public static BuyVehicleCosmeticsMenu getInstance(){
		if(instance == null){
			instance = new BuyVehicleCosmeticsMenu();
		}
		return instance;
	}
	
	private IconMenu menu;
	public BuyVehicleCosmeticsMenu(){
		menu = new IconMenu(ChatColor.YELLOW+"Buy Vehicle Cosmetics", 9, this, GameBlade.plugin);
		Dye d = new Dye();
		d.setColor(DyeColor.RED);
		menu.setOption(0, d.toItemStack(), ChatColor.GOLD+"Vehicle Colours", ChatColor.RED+"Buy Vehicle Colours");
	}
	
	public IconMenu getMenu(){
		return menu;
	}

	@Override
	public void onOptionClick(OptionClickEvent event) {
		final Player player = event.getPlayer();
		event.setWillDestroy(false);
		event.setWillClose(true);
		
		if(event.getPosition() == 0){
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					BuyVehicleColoursMenu.getInstance().getMenu().open(player);
					return;
				}}, 2l);
		}
	}
}
