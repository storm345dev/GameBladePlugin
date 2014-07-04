package org.stormdev.gbplugin.cosmetics.cosmetics.hats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class HatMenu implements Listener {
	//TODO
	private static final int PAGE_SIZE = 9;
	
	private CosmeticManager manager;
	public HatMenu(CosmeticManager manager){
		this.manager = manager;
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	public void open(final Player player){
		//TODO
		player.sendMessage(ChatColor.GRAY+"Opening...");
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				List<Cosmetic> owned = manager.getOwnedCosmetics(player);
				final List<Hat> hats = new ArrayList<Hat>();
				for(Cosmetic c:owned){
					if(c.getType().equals(CosmeticType.HAT) && c instanceof Hat){
						hats.add((Hat)c);
					}
				}
				
				Bukkit.getScheduler().runTask(GameBlade.plugin, new Runnable(){

					@Override
					public void run() {
						createDisplay(player, hats, 1);
						return;
					}});
				return;
			}});
	}
	
	private void createDisplay(final Player player, final List<Hat> owned, final int pageNo){
		int page = pageNo-1;
		final int startPos = (PAGE_SIZE - 3)*page; //if page 1 then (0*52) which is 0, if page 2 then it's 1*52 which is 52
		int endPos = startPos + (PAGE_SIZE - 3); //If startPos = 0, endPos = 52
		
		IconMenu menu = new IconMenu(ChatColor.YELLOW+"Hat Menu", PAGE_SIZE, new OptionClickEventHandler(){

			@Override
			public void onOptionClick(OptionClickEvent event) {
				event.setWillDestroy(false);
				
				int i = event.getPosition();
				
				if(i == 0){
					wearNoHat(player);
					event.setWillClose(true);
					event.setWillDestroy(true);
					return;
				}
				else if(i == (PAGE_SIZE - 2)){
					//Last page
					final int newPage = pageNo-1;
					Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

						@Override
						public void run() {
							createDisplay(player, owned, newPage);
							return;
						}}, 2l);
					event.setWillClose(true);
					event.setWillDestroy(true);
					return;
				}
				else if(i == (PAGE_SIZE - 1)){
					//Next page
					final int newPage = pageNo+1;
					Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

						@Override
						public void run() {
							createDisplay(player, owned, newPage);
							return;
						}}, 2l);
					event.setWillClose(true);
					event.setWillDestroy(true);
					return;
				}
				
				int listPos = startPos + i - 1; //Aka if start is 0 and slot is 0, pos is 0; if start is 52 and slot is 3 then it's 52+3 and listPos = 55
				
				Hat c;
				try {
					c = owned.get(listPos);
				} catch (Exception e) {
					event.setWillClose(false);
					return;
				}
				if(c == null){
					event.setWillClose(false);
					return;
				}
				
				event.setWillClose(true);
				event.setWillDestroy(true);
				onHatClick(player, c);
				return;
			}}, GameBlade.plugin);
		
		int z = 1;
		for(int i=startPos;i<owned.size()&&i<endPos&&z<endPos;i++){
			Hat c = owned.get(i);
			ItemStack display = c.getHeadWear();
			
			menu.setOption(z, display, ChatColor.GOLD+c.getUserFriendlyName(), 
					ChatColor.WHITE+"Wear this hat");
			z++;
		}
		
		menu.setOption(0, new ItemStack(Material.THIN_GLASS), "No Hat", ChatColor.WHITE+"Wear no hat");
		menu.setOption((PAGE_SIZE - 2), new ItemStack(Material.PAPER), "Last Page", ChatColor.GRAY+"<<<<<");
		menu.setOption((PAGE_SIZE - 1), new ItemStack(Material.PAPER), "Next Page", ChatColor.GRAY+">>>>>");
		
		menu.open(player);
	}
	
	public void wearNoHat(Player player){
		//TODO
		player.sendMessage("Debug: wear no hat");
	}
	
	public void onHatClick(Player player, Hat hat){
		player.sendMessage("debug: "+hat.getID());
	}
}
