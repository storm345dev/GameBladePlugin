package org.stormdev.gbplugin.plugin.server;

import org.bukkit.Bukkit;

public class ServerInfo implements org.stormdev.gbapi.servers.ServerInfo{

	@Override
	public double getTPS() {
		return ServerMonitor.getTPS(500);
	}

	@Override
	public int getPlayerCount() {
		return Bukkit.getOnlinePlayers().size();
	}

	@Override
	public int getMaxPlayers() {
		return Bukkit.getMaxPlayers();
	}

	@Override
	public int getResourceScore() {
		return ServerMonitor.getResourceScore();
	}

	@Override
	public double getMaxRam() {
		return round2dp(ServerMonitor.getMaxMemory());
	}

	@Override
	public double getUsedRam() {
		return round2dp(ServerMonitor.getMemoryUse());
	}
	
	private static double round2dp(double toRound){
		double d = toRound*100;
		double i = Math.round(d);
		double dd = i/100;
		return (double) dd;
	}

}
