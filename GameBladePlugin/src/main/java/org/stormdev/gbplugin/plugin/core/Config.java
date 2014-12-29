package org.stormdev.gbplugin.plugin.core;

import java.io.File;

import org.stormdev.gbapi.config.ConfigBase;
import org.stormdev.gbapi.config.ConfigSetting;

public class Config extends ConfigBase {
	public static ConfigSetting<Boolean> colouredLogger = new ConfigSetting<Boolean>("general.logger.colour", true);
	
	public static ConfigSetting<Boolean> overrideChat = new ConfigSetting<Boolean>("general.chat.override", false);
	public static ConfigSetting<Boolean> enableHats = new ConfigSetting<Boolean>("general.hats.enable", true);
	public static ConfigSetting<Boolean> enableFancyCars = new ConfigSetting<Boolean>("general.fancycars.enable", true);
	public static ConfigSetting<Boolean> enableUUIDCorrection = new ConfigSetting<Boolean>("general.uuidCorrection.enable", false);
	
	public static ConfigSetting<Boolean> isLobby = new ConfigSetting<Boolean>("general.isLobby", false);
	
	public static ConfigSetting<String> lobbyServerNameBungee = new ConfigSetting<String>("general.servers.lobby.bungee", "lobby1");
	public static ConfigSetting<String> lobbyServerNameMineManager = new ConfigSetting<String>("general.servers.lobby.minemanager", "GB Lobby 1");
	
	public static ConfigSetting<String> sqlURL = new ConfigSetting<String>("general.sql.url", "jdbc:mysql://localhost/databasename");
	public static ConfigSetting<String> sqlUser = new ConfigSetting<String>("general.sql.user", "root");
	public static ConfigSetting<String> sqlPass = new ConfigSetting<String>("general.sql.pass", "password");
	
	public static ConfigSetting<String> joinRank = new ConfigSetting<String>("general.join.rank", "DEFAULT");
	public static ConfigSetting<String> joinKickMsg = new ConfigSetting<String>("general.join.kickmsg", "This server is currently in a vip only beta stage!");
	
	public static ConfigSetting<String> GBSkullURL = new ConfigSetting<String>("general.serverselector.GBSkullURL", "https://gameblade.net/cdn/skins/gameblade.png");
	
	public Config(File file){
		super(file);
		load();
	}
}
