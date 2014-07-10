package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.CosmeticType;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.plugin.core.Config;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;
import org.stormdev.gbplugin.plugin.utils.MetaValue;

public class HatMenu implements Listener {
	private static final int PAGE_SIZE = 18;
	
	private CosmeticManager manager;
	public HatMenu(CosmeticManager manager){
		this.manager = manager;
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	public void open(final Player player){
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
		if(pageNo < 1){
			createDisplay(player, owned, 1);
			return;
		}
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
		boolean valid = false;
		for(int i=startPos;i<owned.size()&&i<endPos&&z<(endPos+1);i++){
			Hat c = owned.get(i);
			ItemStack display = c.getHeadWear();
			
			menu.setOption(z, display, ChatColor.GOLD+c.getUserFriendlyName(), 
					ChatColor.WHITE+"Wear this hat");
			z++;
			valid = true;
		}
		if(!valid){
			menu.destroy();
			//Go to last page
			final int newPage = pageNo-1;
			Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					createDisplay(player, owned, newPage);
					return;
				}}, 2l);
			return;
		}
		
		menu.setOption(0, new ItemStack(Material.THIN_GLASS), ChatColor.GOLD+"No Hat", ChatColor.WHITE+"Wear no hat");
		menu.setOption((PAGE_SIZE - 2), new ItemStack(Material.PAPER), "Last Page", ChatColor.GRAY+"<<<<<");
		menu.setOption((PAGE_SIZE - 1), new ItemStack(Material.PAPER), "Next Page", ChatColor.GRAY+">>>>>");
		
		menu.open(player);
	}
	
	public void wearNoHat(Player player){
		takeOffHat(player);
	}
	
	public void onHatClick(Player player, Hat hat){
		player.sendMessage(ChatColor.GREEN+"Putting on your hat...");
		setHat(player, hat);
	}
	
	public void setHat(final Player player, final Hat hat){
		runAsync(new Runnable(){

			@Override
			public void run() {
				takeOffHatNow(player);
				try {
					GameBlade.plugin.GBSQL.setInTable(CosmeticManager.SQL_TABLE, CosmeticManager.SQL_ID_KEY, 
							PlayerIDFinder.getMojangID(player).getID(), CosmeticManager.SQL_HAT_KEY, hat.getID());
				} catch (SQLException e) {
					// Oh poo
					e.printStackTrace();
					return;
				}
				
				if(!hat.apply(player)){
					return;
				}
				player.removeMetadata("wearingHat", GameBlade.plugin);
				if(!player.hasMetadata("wearingHat")){
					player.setMetadata("wearingHat", new MetaValue(hat, GameBlade.plugin));
				}
				return;
			}});
	}
	
	private void takeOffHatNow(final Player player){
		try {
			Object o = GameBlade.plugin.GBSQL.searchTable(CosmeticManager.SQL_TABLE, CosmeticManager.SQL_ID_KEY, 
					PlayerIDFinder.getMojangID(player).getID(), CosmeticManager.SQL_HAT_KEY);
			
			if(o != null && !o.toString().equals("null")){
				String cId = o.toString();
				Cosmetic c = GameBlade.plugin.cosmeticManager.get(cId);
				if(c instanceof Hat){
					final Hat hat = (Hat) c;
					Bukkit.getScheduler().runTask(GameBlade.plugin, new Runnable(){

						@Override
						public void run() {
							hat.remove(player);
							player.removeMetadata("wearingHat", GameBlade.plugin);
							return;
						}});
					GameBlade.plugin.GBSQL.setInTable(CosmeticManager.SQL_TABLE, CosmeticManager.SQL_ID_KEY, 
							PlayerIDFinder.getMojangID(player).getID(), CosmeticManager.SQL_HAT_KEY, "null");
				}
			}
		} catch (SQLException e) {
			// Oh poo
			e.printStackTrace();
		}
	}
	
	public void takeOffHat(final Player player){
		runAsync(new Runnable(){

				@Override
				public void run() {
					takeOffHatNow(player);
				}});
	}
	
	private void runAsync(Runnable run){
		if(!Bukkit.isPrimaryThread()){
			run.run();
		}
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, run);
	}
	
	@EventHandler
	void onJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				if(player == null){
					return;
				}
				Object o;
				try {
					o = GameBlade.plugin.GBSQL.searchTable(CosmeticManager.SQL_TABLE, CosmeticManager.SQL_ID_KEY, 
							PlayerIDFinder.getMojangID(player).getID(), CosmeticManager.SQL_HAT_KEY);
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
				
				if(o != null && !o.toString().equals("null")){
					String cId = o.toString();
					Cosmetic c = GameBlade.plugin.cosmeticManager.get(cId);
					if(c instanceof Hat){
						final Hat hat = (Hat) c;
						if(player != null){
							if(!hat.apply(player)){
								return;
							}
							player.removeMetadata("wearingHat", GameBlade.plugin);
							if(!player.hasMetadata("wearingHat")){
								player.setMetadata("wearingHat", new MetaValue(c, GameBlade.plugin));
							}
						}
					}
				}
				
				return;
			}});
	}
	
	@EventHandler
	void invClick(InventoryClickEvent event){
		if(!Config.enableHats.getValue()){
			return;
		}
		Player player;
		try {
			player = (Player) event.getWhoClicked();
		} catch (Exception e) {
			return;
		}
		
		if(!(event.getInventory().getHolder() instanceof Player)){
			return;
		}
		
		Player holder = (Player) event.getInventory().getHolder();
		if(!holder.equals(player)){
			return;
		}
		if(event.getSlotType().equals(SlotType.ARMOR) && event.getRawSlot() == 5 && player.hasMetadata("wearingHat")){
			//They clicked helmet
			player.sendMessage(ChatColor.RED+"Use /hat to take off your hat!");
			event.setCancelled(true);
			player.getInventory().setHelmet(event.getCurrentItem());
			player.closeInventory();
		}
	}
	
	@EventHandler
	void quit(PlayerQuitEvent event){
		if(!Config.enableHats.getValue()){
			return;
		}
		Player player = event.getPlayer();
		if(player.hasMetadata("wearingHat")){
			Object o = player.getMetadata("wearingHat").get(0).value();
			if(!(o instanceof Hat)){
				player.removeMetadata("wearingHat", GameBlade.plugin);
				return;
			}
			
			Hat hat = (Hat) o;
			hat.remove(player);
			player.removeMetadata("wearingHat", GameBlade.plugin);
		}
	}
	
	public static void takeOffHatPhysicallyIfHatIsWorn(Player player){
		if(player.hasMetadata("wearingHat")){
			Object o = player.getMetadata("wearingHat").get(0).value();
			if(!(o instanceof Hat)){
				player.removeMetadata("wearingHat", GameBlade.plugin);
				return;
			}
			
			Hat hat = (Hat) o;
			hat.remove(player);
			player.removeMetadata("wearingHat", GameBlade.plugin);
		}
	}
	
	public static boolean takeOffHatPhysicallyIfHatIsWornAndSay(Player player){
		if(player.hasMetadata("wearingHat")){
			Object o = player.getMetadata("wearingHat").get(0).value();
			if(!(o instanceof Hat)){
				player.removeMetadata("wearingHat", GameBlade.plugin);
				return false;
			}
			
			Hat hat = (Hat) o;
			hat.remove(player);
			player.removeMetadata("wearingHat", GameBlade.plugin);
			return true;
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void onDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		if(player.hasMetadata("wearingHat")){
			ItemStack headWear = player.getInventory().getHelmet();
			Object o = player.getMetadata("wearingHat").get(0).value();
			if(!(o instanceof Hat)){
				return;
			}
			
			Hat hat = (Hat) o;
			hat.remove(player);
			
			List<ItemStack> drops = event.getDrops();
			if(drops.contains(headWear)){
				drops.remove(headWear);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	void onRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		
		if(player.hasMetadata("wearingHat")){
			Object o = player.getMetadata("wearingHat").get(0).value();
			if(!(o instanceof Hat)){
				return;
			}
			
			Hat hat = (Hat) o;
			hat.apply(player);
		}
	}
}
