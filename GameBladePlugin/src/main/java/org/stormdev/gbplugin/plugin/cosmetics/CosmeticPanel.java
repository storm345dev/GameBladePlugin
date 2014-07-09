package org.stormdev.gbplugin.plugin.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CosmeticPanel implements OptionClickEventHandler {
	private static CosmeticPanel instance = null;
	
	public static CosmeticPanel getInstance(){
		if(instance == null){
			instance = new CosmeticPanel();
		}
		return instance;
	}
	
	private IconMenu menu;
	
	public CosmeticPanel(){
		this.menu = new IconMenu(ChatColor.YELLOW+"Cosmetics", 9, this, GameBlade.plugin);
		menu.setOption(0, new ItemStack(Material.GOLD_INGOT), ChatColor.RED+"Buy Cosmetics", ChatColor.GOLD+"Shop for cosmetic items");
		menu.setOption(1, new ItemStack(Material.LEVER), ChatColor.RED+"Manage Cosmetics", ChatColor.GOLD+"Manage your bought cosmetics");
	}
	
	public void open(Player player){
		menu.open(player);
	}

	@Override
	public void onOptionClick(OptionClickEvent event) {
		int pos = event.getPosition();
		event.setWillClose(true);
		event.setWillDestroy(false);
		final Player player = event.getPlayer();
		
		if(pos == 0){ //They selected buy
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					GameBlade.plugin.cosmeticManager.getShop().open(player);
					return;
				}}, 2l);
		}
	}
}
