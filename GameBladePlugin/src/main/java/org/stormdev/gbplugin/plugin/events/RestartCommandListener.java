package org.stormdev.gbplugin.plugin.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.core.AutoRestart;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class RestartCommandListener implements Listener { //As a listener to make SURE we override other plugin's restart commands
	public RestartCommandListener(){
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	void onCommand(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		
		if(event.getMessage().toLowerCase().startsWith("/restart")){
			event.setCancelled(true);
			if(!player.isOp() && !Rank.getRank(player).canUse(Rank.STAFF)){
				return;
			}
			Bukkit.broadcastMessage(ChatColor.GOLD+player.getName()+" scheduled a "+ChatColor.GREEN+"server restart!");
			AutoRestart.forceRestart();
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		
		if(event.getMessage().toLowerCase().startsWith("%restart")){
			event.setCancelled(true);
			if(!player.isOp() && !Rank.getRank(player).canUse(Rank.STAFF)){
				return;
			}
			Bukkit.broadcastMessage(ChatColor.GOLD+player.getName()+" scheduled a "+ChatColor.GREEN+"server restart!");
			AutoRestart.forceRestart();
		}
	}
	
}
