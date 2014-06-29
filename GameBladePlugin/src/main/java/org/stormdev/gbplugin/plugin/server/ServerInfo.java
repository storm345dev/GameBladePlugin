package org.stormdev.gbplugin.plugin.server;

import org.bukkit.Bukkit;

public class ServerInfo implements org.stormdev.gbapi.servers.ServerInfo{

	@Override
	public double getTPS() {
		return ServerMonitor.getTPS(500);
	}

	@Override
	public int getPlayerCount() {
		return Bukkit.getOnlinePlayers().length;
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
		return ServerMonitor.getMaxMemory();
	}

	@Override
	public double getUsedRam() {
		return ServerMonitor.getMemoryUse();
	}

}
