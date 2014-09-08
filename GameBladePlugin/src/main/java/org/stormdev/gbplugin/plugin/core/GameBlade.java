package org.stormdev.gbplugin.plugin.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.gbapi.core.GameBladeAPI;
import org.stormdev.gbapi.links.LinkShortener;
import org.stormdev.gbapi.links.LinkShortener.ShorteningError;
import org.stormdev.gbapi.storm.SQL.MySQL;
import org.stormdev.gbapi.storm.SQL.SQLManager;
import org.stormdev.gbplugin.api.stars.GiveStarsCommand;
import org.stormdev.gbplugin.api.stars.MyStarsCommand;
import org.stormdev.gbplugin.api.villagers.VillagerManager;
import org.stormdev.gbplugin.bans.BanHandle;
import org.stormdev.gbplugin.bans.BanListener;
import org.stormdev.gbplugin.plugin.chat.ChatManager;
import org.stormdev.gbplugin.plugin.commands.BroadcastCommandExecutor;
import org.stormdev.gbplugin.plugin.commands.CreateVillagerCommand;
import org.stormdev.gbplugin.plugin.commands.HatCommand;
import org.stormdev.gbplugin.plugin.commands.InvSeeCommand;
import org.stormdev.gbplugin.plugin.commands.ModCommandExecutor;
import org.stormdev.gbplugin.plugin.commands.PunishCommand;
import org.stormdev.gbplugin.plugin.commands.SetRankCommand;
import org.stormdev.gbplugin.plugin.commands.StarShopCommand;
import org.stormdev.gbplugin.plugin.commands.UnbanCommand;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticVillagers;
import org.stormdev.gbplugin.plugin.cosmetics.carts.CartFillListener;
import org.stormdev.gbplugin.plugin.cosmetics.wear.hats.HatMenu;
import org.stormdev.gbplugin.plugin.events.ServerJoinListener;
import org.stormdev.gbplugin.plugin.mkTokens.TokenChecker;
import org.stormdev.gbplugin.plugin.modpanel.ModMenu;
import org.stormdev.gbplugin.plugin.server.ServerInfo;
import org.stormdev.gbplugin.plugin.server.ServerMonitor;
import org.stormdev.gbplugin.plugin.server.uuidcorrector.UUIDListener;
import org.stormdev.gbplugin.plugin.utils.JarUtils;
import org.stormdev.servermanager.api.APIProvider;
import org.stormdev.servermanager.api.ServerManagerAPI;
import org.stormdev.tokenhandler.commands.GiveTokensCommand;
import org.stormdev.tokenhandler.commands.MyTokensCommand;


public class GameBlade extends JavaPlugin implements PluginMessageListener {
	public static CustomLogger logger;
	public static GameBlade plugin;
	public static GameBladeAPI api;
	public static Config config;
	public static Random random = new Random();
	public static ServerInfo serverInfo;
	public static ServerManagerAPI smApi;
	public static BanHandle banHandler;
	
	public String starsURL = "http://gamebla.de/stars";
	public BukkitTask serverMonitor;
	public ServerSelector selector;
	public CosmeticManager cosmeticManager;
	
	private ServerMonitor serverStats;
	
	public SQLManager GBSQL;
	
	@Override
	public void onEnable(){
		GameBlade.plugin = this;
		
		config = new Config(new File(getDataFolder()+File.separator+"config.yml"));
		
		logger = new CustomLogger(Bukkit.getConsoleSender(), getLogger());
		
		if(!initAPI()){
			// Error loading api
			logger.info("Error enabling GameBladePlugin! :(");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		JarUtils.loadLibs(); //Loads fanciful JSON library
		
		try {
			Class.forName("mkremins.fanciful.FancyMessage");
			logger.info("Loaded fanciful(JSON chat library) successfully!");
		} catch (ClassNotFoundException e) {
			logger.info(ChatColor.RED+"Failed to load fanciful!");
		}
		
		serverStats = new ServerMonitor();
		serverStats.start();
		serverInfo = new ServerInfo();
		
		smApi = APIProvider.getAPI();
		selector = new ServerSelector(this);
		
		setupCmds();
		setupListeners();
		
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
        GBSQL = new SQLManager(new MySQL(this, Config.sqlURL.getValue(), Config.sqlUser.getValue(), Config.sqlPass.getValue()), this);
        GBSQL.checkConnection();
        GBSQL.createTable("stars", new String[]{"uuid"
        		,"stars"
        }, new String[]{"varchar(255) NOT NULL PRIMARY KEY", "int(20)"});
        
        banHandler = new BanHandle();
        cosmeticManager = new CosmeticManager();
        
        Bukkit.getScheduler().runTaskAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				try {
					starsURL = LinkShortener.shorten("http://store.gameblade.net/category/309777", "stars");
					logger.info("Using stars URL as "+starsURL);
				} catch (ShorteningError e) {
					e.printStackTrace();
				}
				return;
			}});
        
		logger.info("GameBladePlugin "+ChatColor.GREEN+"enabled!");
	}
	
	@Override
	public void onDisable(){
		for(Player player:Bukkit.getOnlinePlayers()){
			HatMenu.takeOffHatPhysicallyIfHatIsWorn(player);
		}
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
		
		if(Bukkit.getPluginManager().getPlugin("MarioKart") != null){
			try {
				Class.forName("net.stormdev.mario.server.EconProvider");
				try {
					TokenChecker.handleMarioKart(); //Make MarioKart use tokens
				} catch (NoClassDefFoundError e) {
					//Who cares? There just isn't MarioKart...
				}
			} catch (ClassNotFoundException e1) {
				//No MarioKart...
			}
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
		getCommand("broadcast").setExecutor(new BroadcastCommandExecutor(this));
		getCommand("servers").setExecutor(selector);
		getCommand("mytokens").setExecutor(new MyTokensCommand());
		getCommand("givetokens").setExecutor(new GiveTokensCommand());
		getCommand("mystars").setExecutor(new MyStarsCommand());
		getCommand("givestars").setExecutor(new GiveStarsCommand());
		getCommand("ban").setExecutor(new PunishCommand());
		getCommand("kick").setExecutor(new PunishCommand());
		getCommand("warn").setExecutor(new PunishCommand());
		getCommand("pardon").setExecutor(new UnbanCommand());
		getCommand("starshop").setExecutor(new StarShopCommand());
		getCommand("hat").setExecutor(new HatCommand());
		getCommand("createmenuvillager").setExecutor(new CreateVillagerCommand());
		getCommand("invsee").setExecutor(new InvSeeCommand());
		getCommand("setrank").setExecutor(new SetRankCommand());
	}
	
	private void setupListeners(){
		ModMenu.init();
		new UUIDListener();
		new ChatManager();
		new BanListener();
		new CartFillListener();
		new VillagerManager();
		new ServerJoinListener();
		CosmeticVillagers.register();
	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
		return; //Not needed
	}
}
