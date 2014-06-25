package org.stormdev.gbplugin.plugin.core;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.stormdev.gbapi.core.APIProvider;
import org.stormdev.gbapi.core.GameBladeAPI;


public class GameBlade extends JavaPlugin {
	public static CustomLogger logger;
	public static GameBlade plugin;
	public static GameBladeAPI api;
	
	@Override
	public void onEnable(){
		plugin = this;
		logger = new CustomLogger(Bukkit.getConsoleSender(), getLogger());
		
		if(!initAPI()){
			// Error loading api
			logger.info("Error enabling GameBladePlugin! :(");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		logger.info("GameBladePlugin enabled!");
	}
	
	@Override
	public void onDisable(){
		logger.info("GameBladePlugin disabled!");
	}
	
	private boolean initAPI(){
		if(!checkDependencies()){
			return false;
		}
		
		try {
			Class<?> c = Class.forName("org.stormdev.gbapi.core.APIProvider");
			Method m = c.getMethod("setAPI", GameBladeAPI.class);
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
}
