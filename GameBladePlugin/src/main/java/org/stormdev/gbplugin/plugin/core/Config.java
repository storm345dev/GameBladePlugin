package org.stormdev.gbplugin.plugin.core;

import java.io.File;

import org.stormdev.gbapi.config.ConfigBase;
import org.stormdev.gbapi.config.ConfigSetting;

public class Config extends ConfigBase {
	public static ConfigSetting<Boolean> colouredLogger = new ConfigSetting<Boolean>("general.logger.colour", true);
	public static ConfigSetting<String> lobbyServerNameBungee = new ConfigSetting<String>("general.servers.lobby.bungee", "lobby1");
	public static ConfigSetting<String> lobbyServerNameMineManager = new ConfigSetting<String>("general.servers.lobby.minemanager", "GB Lobby 1");
	
	public Config(File file){
		super(file);
		load();
	}
}
