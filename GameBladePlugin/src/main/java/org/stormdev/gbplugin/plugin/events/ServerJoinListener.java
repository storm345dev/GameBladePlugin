package org.stormdev.gbplugin.plugin.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.stormdev.gbapi.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.ranks.RankSQL;

public class ServerJoinListener implements Listener {
	
	public ServerJoinListener(){
		Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
	}
	
	@EventHandler
	void onFullJoin(AsyncPlayerPreLoginEvent event){
		UUID uuid = event.getUniqueId();
		if(event.getLoginResult().equals(Result.KICK_FULL)){
			if(RankSQL.getRankByUUID(PlayerIDFinder.toUUIDString(uuid)).getCosmeticRank().canUse(Rank.VIP_PLUS)){
				event.setLoginResult(Result.ALLOWED);
				event.allow();
			}
		}
	}
}
