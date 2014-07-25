package org.stormdev.gbplugin.plugin.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.Config;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.carts.VehicleColoursMenu;

public class CosmeticManagePanel implements OptionClickEventHandler {
	private static CosmeticManagePanel instance = null;
	
	public static CosmeticManagePanel getInstance(){
		if(instance == null){
			instance = new CosmeticManagePanel();
		}
		return instance;
	}
	
	private IconMenu menu;
	
	public CosmeticManagePanel(){
		this.menu = new IconMenu(ChatColor.YELLOW+"Manage Cosmetics", 9, this, GameBlade.plugin);
		menu.setOption(0, new ItemStack(Material.PUMPKIN), ChatColor.RED+"Hats", ChatColor.GOLD+"Manage your owned hats");
		menu.setOption(1, new ItemStack(Material.MINECART), ChatColor.RED+"Vehicle Paint", ChatColor.GOLD+"Manage your vehicle colour");
		menu.setOption(8, new ItemStack(Material.PAPER), ChatColor.RED+"My Stars", ChatColor.GOLD+"Click to see how many stars you have"
				,"",
				ChatColor.RED+"About Stars:",
				ChatColor.GOLD+"Stars are a currency",
				ChatColor.GOLD+"used only for cosmetics.",
				ChatColor.WHITE+"Buy stars at http://store.gameblade.net");
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
		
		if(pos == 0){ //They selected hats
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					if(!Config.enableHats.getValue()){
						player.sendMessage(ChatColor.RED+"Hats are disabled while on this server. Go to another, such as a lobby, to use hats.");
						return;
					}
					GameBlade.plugin.cosmeticManager.getHatMenu().open(player);
					return;
				}}, 2l);
		}
		else if(pos == 1){ //They selected vehicle colours
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					VehicleColoursMenu.getInstance().getMenu().open(player);
					return;
				}}, 2l);
		}
		else if(pos == 8){ //They selected manage
			player.sendMessage(ChatColor.GRAY+"Loading...");
			player.performCommand("mystars");
		}
	}
}
