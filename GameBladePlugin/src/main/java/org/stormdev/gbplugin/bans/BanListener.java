package org.stormdev.gbplugin.bans;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}
