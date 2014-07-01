package org.stormdev.gbplugin.plugin.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.gbapi.core.GameBladeAPI;
import org.stormdev.gbplugin.plugin.commands.ModCommandExecutor;
import org.stormdev.gbplugin.plugin.modpanel.ModMenu;
import org.stormdev.gbplugin.plugin.server.ServerInfo;
import org.stormdev.gbplugin.plugin.server.ServerMonitor;


public class GameBlade extends JavaPlugin {
	public static CustomLogger logger;
	public static GameBlade plugin;
	public static GameBladeAPI api;
	public static Config config;
	public static Random random = new Random();
	public static ServerInfo serverInfo;
	
	public BukkitTask serverMonitor;
	
	private ServerMonitor serverStats;
	
	@Override
	public void onEnable(){
		plugin = this;
		
		config = new Config(new File(getDataFolder()+File.separator+"config.yml"));
		
		logger = new CustomLogger(Bukkit.getConsoleSender(), getLogger());
		
		if(!initAPI()){
			// Error loading api
			logger.info("Error enabling GameBladePlugin! :(");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		serverStats = new ServerMonitor();
		serverStats.start();
		serverInfo = new ServerInfo();
		
		setupCmds();
		setupListeners();
		
		logger.info("GameBladePlugin "+ChatColor.GREEN+"enabled!");
	}
	
	@Override
	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
		logger.info("GameBladePlugin "+ChatColor.RED+"disabled!");
	}
	
	private boolean initAPI(){
		if(!checkDependencies()){
			return false;
		}
		
		try {
			Class<?> c = Class.forName("org.stormdev.gbapi.core.APIProvider");
			Method m = c.getDeclaredMethod("setAPI", GameBladeAPI.class);
			m.setAccessible(true);
			m.invoke(null, loadAPI());
		} catch (Exception e) {
			logger.info("Bad SecurityManager OR Bad GameBladePlugin file, doesn't include the API! :( Please try a different plugin file!");
			return false;
		}
		
		return true;
	}
	
	private boolean checkDependencies(){
		try {
			Class.forName("org.stormdev.servermanager.api.APIProvider");
		} catch (ClassNotFoundException e) {
			logger.info("You need to have a valid SMCore.jar installed on the server (With the api) to run this plugin!");
			return false;
		}
		
		return true;
	}
	
	private GameBladeAPI loadAPI(){
		//Initialise and return an instance of GameBladeAPI
		api = new org.stormdev.gbplugin.api.core.GameBladeAPI();
		return api;
	}
	
	private void setupCmds(){
		getCommand("mod").setExecutor(new ModCommandExecutor(this));
	}
	
	private void setupListeners(){
		ModMenu.init();
	}
}
