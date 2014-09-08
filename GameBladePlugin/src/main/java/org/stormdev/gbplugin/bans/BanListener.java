package org.stormdev.gbplugin.bans;

import im.mta.coremanager.modmenu.ModSettingsMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.stormdev.gbapi.bans.BanHandler.Time;
import org.stormdev.gbapi.core.APIProvider;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.utils.MetaValue;

public class BanListener implements Listener {
	public BanListener(){
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event){
		
		final Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				if(APIProvider.getAPI().getBans().isBanned(player)){
					final Time duration = APIProvider.getAPI().getBans().getBanDuration(player);
					player.setMetadata("banned", new MetaValue(null, GameBlade.plugin));
					Bukkit.getScheduler().runTask(GameBlade.plugin, new Runnable(){

						@Override
						public void run() {
							player.kickPlayer("Ban time remaining: "+duration.getRemainingTime());
							return;
						}});
				}
				else {
					//Join message
					Rank r = Rank.getRank(player);
					if(r.canUse(Rank.PREMIUM_PLUS) && !r.canUse(Rank.ULTIMATE) && !ModSettingsMenu.vanishArray.contains(player.getName())){
						//VIP join message
						Bukkit.broadcastMessage(ChatColor.DARK_RED+"[*] "+ChatColor.GOLD+player.getName()+" joined the server.");
					}
					else if(r.canUse(Rank.ULTIMATE)){
						Bukkit.broadcastMessage(ChatColor.GOLD+"[*] "+ChatColor.GREEN+"[ULTIMATE]"+ChatColor.AQUA+player.getName()+" joined the server.");
					}
				}
				return;
			}});
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	void onJoin(PlayerJoinEvent event){
		event.setJoinMessage(null);
	}
	
	@EventHandler
	void onLeave(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(player.hasMetadata("gameblade.chatinput")){
			Object o = player.getMetadata("gameblade.chatinput");
			if(o instanceof ChatInput){
				ChatInput c = (ChatInput) o;
				c.destroy();
			}
			player.removeMetadata("gameblade.chatinput", GameBlade.plugin);
		}
		if(player.hasMetadata("banned")){
			player.removeMetadata("banned", GameBlade.plugin);
			event.setQuitMessage(null);
		}
		else {
			//Leave msg
			event.setQuitMessage(null);
		}
	}
	
	@EventHandler
	public void onQuitBanPanel(InventoryCloseEvent event){
		Entity e = event.getPlayer();
		if(!(e instanceof Player)){
			return;
		}
		Player player = (Player)e;
		if(player.hasMetadata(BanPanel.meta)){
			Object o = player.getMetadata(BanPanel.meta).get(0).value();
			player.removeMetadata(BanPanel.meta, GameBlade.plugin);
			
			if(o instanceof BanPanel){
				((BanPanel)o).onClose();
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(player.hasMetadata(ChatInput.meta)){
			Object o = player.getMetadata(ChatInput.meta).get(0).value();
			player.removeMetadata(ChatInput.meta, GameBlade.plugin);
			if(o instanceof ChatInput){
				((ChatInput)o).onChat(player, event.getMessage());
				event.setMessage("");
				event.setCancelled(true);
			}
		}
	}
}
