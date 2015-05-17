package org.stormdev.gbplugin.plugin.stormapis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.stormdev.gbapi.core.APIProvider;
import org.stormdev.gbapi.storm.UUIDAPI.PlayerIDFinder;
import org.stormdev.gbplugin.plugin.core.Config;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class RemoteTokens implements /*SMListener, */org.stormdev.gbapi.storm.tokens.Tokens { //Requires ServerManagerAPI
	private static RemoteTokens instance;
	/*public static ServerManagerAPI api= null;*/
	public static String GB_LOBBY_SERVER_ID = "GB Lobby 1";
	private Plugin plugin;
	
	public static RemoteTokens getInstance(){
		if(instance == null){
			instance = new RemoteTokens();
		}
		return instance;
	}
	
	/*private MessageRecipient lobbyServer;*/
	private Map<String, Integer> tokenQueries = new HashMap<String, Integer>();
	
	public RemoteTokens(){
		this.plugin = GameBlade.plugin;
		GB_LOBBY_SERVER_ID = Config.lobbyServerNameMineManager.getValue();
		/*Bukkit.getScheduler().runTaskTimerAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() { //Stops potential memory leak in tokenQueries
				if(tokenQueries.size() > 50){ //TOO many queries, probably unable to connect
					List<String> keys = new ArrayList<String>(tokenQueries.keySet());
					long timeoutTime = System.currentTimeMillis()+10000;
					
					int i = 0;
					while(tokenQueries.size() > 50 && System.currentTimeMillis() < timeoutTime){
						try {
							if(i>=keys.size()){
								return;
							}
							String key = keys.get(i);
							tokenQueries.remove(key);
						} catch (Exception e) {
							//Meh, error
						}
						i++;
					}
				}
				return;
			}}, 20*60*20l, 20*60*20l);*/
		
		/*if(Bukkit.getPluginManager().getPlugin("ServerManager") == null){
			plugin.getLogger().info("Sorry this plugin requires ServerManager!");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			throw new RuntimeException("ServerManager not found!");
		}
		
		try {
			Class.forName("org.stormdev.servermanager.api.APIProvider");
		} catch (ClassNotFoundException e) {
			plugin.getLogger().info("Sorry this plugin requires ServerManager with the API!");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			throw new RuntimeException("ServerManager not found!");
		}
		
		api = APIProvider.getAPI();
		if(api == null){
			plugin.getLogger().info("Sorry this plugin requires ServerManager with the API!");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			throw new RuntimeException("ServerManager not found!");
		}
		
		if(!api.getProvider().equals(APIProviderType.CORE)){
			plugin.getLogger().info("Sorry this plugin requires ServerManagerCore with the API! You aren't using 'CORE'");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			throw new RuntimeException("ServerManager not found!");
		}
		
		lobbyServer = MessageRecipient.create(GB_LOBBY_SERVER_ID);
		api.getEventManager().registerListener(this);*/
	}
	
	@Override
	public int getTokens(Player player) throws TokenServiceUnavailableException{
		if(Bukkit.isPrimaryThread()){
			throw new RuntimeException("Please don't lookup tokens in the main thread!");
		}
		
		/*String uuid = PlayerIDFinder.getMojangID(player).getID();
		
		tokenQueries.put(uuid, -1);
		
		try {
			api.getMessenger().sendMessage(lobbyServer, "getTokens", uuid);
		} catch (Exception e) {
			throw new RuntimeException("Failed to determine token balacne for "+player.getName());
		}
		
		int delay = 300; //15s
		while(tokenQueries.get(uuid) < 0 && delay > 0){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				//Whatever
			}
			
			delay--;
		}
		
		int tokens = tokenQueries.get(uuid);
		if(tokens < 0){
			throw new TokenServiceUnavailableException();
		}
		return tokens;*/
		return 0;
	}
	
	@Override
	public void awardTokens(final String playerName, final int tokens){
		/*Runnable run = new Runnable(){

			public void run() {
				String uuid = PlayerIDFinder.getMojangID(playerName).getID();
				
				try {
					api.getMessenger().sendMessage(lobbyServer, "awardTokens", tokens+"|"+uuid);
				} catch (Exception e) {
					//Oh well :( They don't get their tokens
					System.out.println("Failed to give "+playerName+" their awarded tokens :(  ("+tokens+")");
				}
				return;
			}};
			
		if(Bukkit.isPrimaryThread()){
			Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
		}
		else {
			run.run();
		}*/
	}
	
	@Override
	public void awardTokens(final Player player, final int tokens){
		/*Runnable run = new Runnable(){

			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				
				try {
					api.getMessenger().sendMessage(lobbyServer, "awardTokens", tokens+"|"+uuid);
				} catch (Exception e) {
					//Oh well :( They don't get their tokens
					System.out.println("Failed to give "+player.getName()+" their awarded tokens :(  ("+tokens+")");
				}
				return;
			}};
			
		if(Bukkit.isPrimaryThread()){
			Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
		}
		else {
			run.run();
		}*/
	}
	
	@Override
	public void takeTokens(final String playerName, final int tokens){
		/*Runnable run = new Runnable(){

			public void run() {
				String uuid = PlayerIDFinder.getMojangID(playerName).getID();
				
				try {
					api.getMessenger().sendMessage(lobbyServer, "takeTokens", tokens+"|"+uuid);
				} catch (Exception e) {
					//Oh well :( They don't get their tokens
					System.out.println("Failed to give "+playerName+" their awarded tokens :(  ("+tokens+")");
				}
				return;
			}};
			
		if(Bukkit.isPrimaryThread()){
			Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
		}
		else {
			run.run();
		}*/
	}
	
	@Override
	public void takeTokens(final Player player, final int tokens){
		/*Runnable run = new Runnable(){

			public void run() {
				String uuid = PlayerIDFinder.getMojangID(player).getID();
				
				try {
					api.getMessenger().sendMessage(lobbyServer, "takeTokens", tokens+"|"+uuid);
				} catch (Exception e) {
					//Oh well :( They don't get their tokens
					System.out.println("Failed to give "+player.getName()+" their awarded tokens :(  ("+tokens+")");
				}
				return;
			}};
			
		if(Bukkit.isPrimaryThread()){
			Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
		}
		else {
			run.run();
		}*/
	}
	
	/*@SMEventHandler
	void messageReceive(MessageReceiveEvent event){
		if(event.getMessage().getTitle().equals("tokenBalance")){
			String msg = event.getMessage().getMessage();
			
			int split = msg.indexOf("|");
			int tokens = 0;
			String uuid = "";
			if(split < 1){
				//Tokens invalid string
				uuid = msg; //Maybe?
			}
			else {
				try {
					String tokensRaw = msg.substring(0, split);
					uuid = msg.substring(split+1);
					tokens = Integer.parseInt(tokensRaw);
				} catch (Exception e) {
					tokens = 0;
				}
			}
			
			//Handle 'tokens' passing back to method
			if(tokenQueries.containsKey(uuid)){
				tokenQueries.put(uuid, tokens);
			}
			
			return;
		}
	}*/
}
