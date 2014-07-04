package org.stormdev.gbplugin.bans;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.stormdev.gbapi.bans.BanHandler.Time;
import org.stormdev.gbapi.core.APIProvider;
import org.stormdev.gbplugin.plugin.core.GameBlade;

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
					Time duration = APIProvider.getAPI().getBans().getBanDuration(player);
					player.kickPlayer("Ban time remaining: "+duration.getRemainingTime());
				}
				return;
			}});
		
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
