package org.stormdev.gbplugin.plugin.cosmetics.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.stormdev.gbplugin.cosmetics.cosmetics.hats.Hat;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class CosmeticShop implements Listener {
	private static final int PAGE_SIZE = 54; //54 is a double chest
	private IconMenu cosmeticMenu;
	private Map<Integer, IconMenu> hatMenuPages = new HashMap<Integer, IconMenu>();
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
		updateHatShop();
	}
	
	private void updateHatShop(){
		final List<Cosmetic> hats = manager.getAllByType(CosmeticType.HAT);
		double amountOfHats = hats.size();
		double pgs = amountOfHats/(PAGE_SIZE - 2); //One page can hold ((6*9)-2) 52 items.
		int pages = (int) Math.ceil(pgs);
		for(int i=1;i<=pages;i++){
			updateHatShop(i);
		}
	}
	
	private void updateHatShop(final int pageNo){
		int page = pageNo;
		page--; //If page 1, start on 0
		final int startPos = (PAGE_SIZE - 2)*page; //if page 1 then (0*52) which is 0, if page 2 then it's 1*52 which is 52
		int endPos = startPos + (PAGE_SIZE - 2); //If startPos = 0, endPos = 52
		
		final List<Cosmetic> hats = manager.getAllByType(CosmeticType.HAT);
		IconMenu menu = new IconMenu(ChatColor.YELLOW+"Hat Menu", PAGE_SIZE, new OptionClickEventHandler(){

			@Override
			public void onOptionClick(OptionClickEvent event) {
				event.setWillDestroy(false);
				
				int i = event.getPosition();
				
				if(i == (PAGE_SIZE - 2)){
					//Last page
					int newPage = pageNo-1;
					if(hatMenuPages.containsKey(newPage)){
						openLater(hatMenuPages.get(newPage), event.getPlayer());
						event.setWillClose(true);
						return;
					}
					event.setWillClose(false);
					return;
				}
				else if(i == (PAGE_SIZE - 1)){
					//Next page
					int newPage = pageNo+1;
					if(hatMenuPages.containsKey(newPage)){
						openLater(hatMenuPages.get(newPage), event.getPlayer());
						event.setWillClose(true);
						return;
					}
					event.setWillClose(false);
					return;
				}
				
				int listPos = startPos + i; //Aka if start is 0 and slot is 0, pos is 0; if start is 52 and slot is 3 then it's 52+3 and listPos = 55
				
				Cosmetic c = hats.get(listPos);
				if(c == null){
					event.setWillClose(false);
					return;
				}
				
				event.setWillClose(true);
				manager.purchaseCosmetic(event.getPlayer(), c);
				return;
			}}, GameBlade.plugin);
		
		int z = 0;
		for(int i=startPos;i<hats.size()&&i<endPos&&z<endPos;i++){
			Cosmetic c = hats.get(i);
			ItemStack display = new ItemStack(Material.PUMPKIN);
			if(c instanceof Hat){
				display = ((Hat)c).getHeadWear();
			}
			
			boolean ranked = !c.minimumRank().equals(Rank.DEFAULT);
			boolean free = c.getPrice() <= 0;
			String price = free?"Free":c.getPrice()+" "+c.getCurrencyUsed().getName();
			
			if(!ranked){
				menu.setOption(z, display, ChatColor.GOLD+c.getUserFriendlyName(), 
					ChatColor.RED+"Cost:", ChatColor.WHITE+price);
			}
			else {
				menu.setOption(z, display, ChatColor.GOLD+c.getUserFriendlyName(), 
						ChatColor.RED+"Cost:", ChatColor.WHITE+price
						, ChatColor.YELLOW+c.minimumRank().getName()+" or higher");
			}
			z++;
		}
		
		menu.setOption((PAGE_SIZE - 2), new ItemStack(Material.PAPER), "Last Page", ChatColor.GRAY+"<<<<<");
		menu.setOption((PAGE_SIZE - 1), new ItemStack(Material.PAPER), "Next Page", ChatColor.GRAY+">>>>>");
		
		hatMenuPages.put(pageNo, menu);
	}
	
	public void open(Player player){
		cosmeticMenu.open(player);
	}
	
	public void openHatMenu(final Player player){
		Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				hatMenuPages.get(1).open(player);
				return;
			}}, 2l);
	}
	
	private void openLater(final IconMenu menu, final Player player){
		Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				menu.open(player);
				return;
			}}, 2l);
	}
	
	
}