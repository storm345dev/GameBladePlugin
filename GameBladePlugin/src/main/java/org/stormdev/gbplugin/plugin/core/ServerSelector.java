package org.stormdev.gbplugin.plugin.core;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.server.ping.ServerListPing;
import org.stormdev.gbplugin.plugin.server.ping.ServerListPing.StatusResponse;
import org.stormdev.gbplugin.plugin.utils.Colors;
import org.stormdev.servermanager.api.messaging.Server;

public class ServerSelector implements CommandExecutor, Listener, OptionClickEventHandler
{
	private static final String INVENTORY_TITLE = ChatColor.RED + "Choose a server";
	
	private static IconMenu menu = null;
	
	private static GameBlade plugin;
	
	private static List<SelectableServer> rootServers = new ArrayList<SelectableServer>();
	
	public static interface SelectableServer {
		public String getDisplayName();
		public List<String> getLore();
		public void onSelect(OptionClickEvent event, Player player);
		public void update(IconMenu menu);
		public boolean isChild();
		public MultipleServer getParent();
		public int getSlot();
	}
	
	private static class MultipleServer implements SelectableServer, OptionClickEventHandler {
		private String name;
		private List<String> lore;
		/*private String bungeePrefix; //eg. 'mk' matches, 'mk1' and 'mk2'
*/		private String mineManagerPrefix; //Eg. 'GB MarioKart' matches 'GB MarioKart 1', etc...
		/*private String ip = "localhost"; //Eg. 'alpha.gameblade.net' and 'bravo.gameblade.net'
		private int minPort;
		private int maxPort;*/
		private int online;
		private int maxPlayers;
		private List<SelectableServer> subServers = new ArrayList<SelectableServer>();
		private IconMenu subMenu;
		private ItemStack icon;
		private int slot;
		
		public MultipleServer(ItemStack icon, int slot, int invSize, String displayName,/* String bungeePrefix,*/ String mineManagerPrefix, String... lore){
			this.icon = icon;
			this.slot = slot;
			this.name = displayName;
			/*this.bungeePrefix = bungeePrefix;*/
			this.mineManagerPrefix = mineManagerPrefix;
			this.lore = new ArrayList<String>(Arrays.asList(lore));
			
			this.subMenu = new IconMenu(name, invSize, this, plugin);
			
			rootServers.add(this);
			
			task();
			callUpdate(this);
		}
		
		private void task(){
			Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable(){

				@Override
				public void run() {
				        int online = 0;
				        int max = 0;
				        if(GameBlade.smApi != null){
				        	//ServerID = "GB MTASA 1";
				        	Map<String, Server> srvs = GameBlade.smApi.getServers().getServers();
				        	if(srvs != null && srvs.size() > 0){
				        		for(String s:new ArrayList<String>(srvs.keySet())){
				        			if(s.trim().toLowerCase().startsWith(mineManagerPrefix.trim().toLowerCase())){
				        				Server se = srvs.get(s);
				        				if(se != null){
				        					online += se.getOnlinePlayerCount();
				        					max += se.getMaxPlayers();
				        				}
				        			}
				        		}
				        	}
				        }
				        MultipleServer.this.maxPlayers = max;
				        MultipleServer.this.online = online;
				        Bukkit.getScheduler().runTask(plugin, new Runnable(){

							@Override
							public void run() {
								callUpdate(MultipleServer.this);
								return;
							}});
					return;
				}}, 0l, 10*20l);
		}
		
		@Override
		public String getDisplayName() {
			return name;
		}

		@Override
		public List<String> getLore() {
			List<String> lore = new ArrayList<String>();
			lore.addAll(this.lore);
			lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
			lore.add(ChatColor.WHITE+""+online+"/"+maxPlayers);
			return lore;
		}

		@Override
		public void onSelect(OptionClickEvent event, final Player player) {
			player.sendMessage(ChatColor.GRAY+"Opening...");
			player.closeInventory();
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){

				@Override
				public void run() {
					subMenu.open(player);
					return;
				}}, 2l);
		}

		@Override
		public void update(IconMenu menu) {
			menu.setOption(slot, icon.clone(), getDisplayName(), getLore());
		}

		@Override
		public boolean isChild() {
			return false;
		}

		@Override
		public MultipleServer getParent() {
			return null;
		}

		@Override
		public int getSlot() {
			return slot;
		}

		@Override
		public void onOptionClick(OptionClickEvent event) {
			event.setWillClose(true);
			event.setWillDestroy(false);
			
			int slot = event.getPosition();
			for(SelectableServer ss:subServers){
				if(ss.getSlot() == slot){
					ss.onSelect(event, event.getPlayer());
				}
			}
		}
		
	}
	
	private static class SingleServer implements SelectableServer {
		private String displayName; //Eg. Survival
		private List<String> lore;
		private String bungeeName; //Eg. survival would match survival1, survival2, etc...
		private String mineManagerNamePrefix; //Eg. GB Survival would match GB Survival 1, etc...
		private int maxPlayers = 50;
		private int online = 0;
		private int slot;
		private ItemStack icon;
		private boolean child = false;
		private MultipleServer parent = null;
		private boolean isOnline;
		
		private String ip = null;
		private int port = -1;
		private String MOTD = null;
		private String offlineMsg = ChatColor.RED+"Restarting...";
		
		public SingleServer(ItemStack icon, int slot, String displayName, String bungeeName, String mineManagerPrefix, String... lore){
			this(icon, slot, displayName, bungeeName, mineManagerPrefix, null, lore);
		}
		
		public SingleServer(ItemStack icon, int slot, String displayName, String bungeeName, String mineManagerPrefix, MultipleServer parent, String... lore){
			this.icon = icon;
			this.slot = slot;
			this.displayName = displayName;
			this.bungeeName = bungeeName;
			this.mineManagerNamePrefix = mineManagerPrefix;
			this.lore = new ArrayList<String>(Arrays.asList(lore));
			
			if(parent != null){
				this.child = true;
				this.parent = parent;
				parent.subServers.add(this);
			}
			else {
				rootServers.add(this);
			}
			
			task();
			callUpdate(this);
		}
		
		public void setOfflineMessage(String message){
			this.offlineMsg = message;
		}
		
		public void setUseMOTD(String ip, int port){
			this.ip = ip;
			this.port = port;
		}
		
		private void task(){
			Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable(){

				@Override
				public void run() {
				        int online = 0;
				        int max = 0;
				        boolean isOnline = false;
				        if(GameBlade.smApi != null){
				        	//ServerID = "GB MTASA 1";
				        	Map<String, Server> srvs = GameBlade.smApi.getServers().getServers();
				        	if(srvs != null && srvs.size() > 0){
				        		for(String s:new ArrayList<String>(srvs.keySet())){
				        			if(s.trim().toLowerCase().startsWith(mineManagerNamePrefix.trim().toLowerCase())){
				        				Server se = srvs.get(s);
				        				if(se != null){
				        					online += se.getOnlinePlayerCount();
				        					max += se.getMaxPlayers();
				        					isOnline = true;
				        				}
				        			}
				        		}
				        	}
				        }
				        SingleServer.this.isOnline = isOnline;
				        SingleServer.this.maxPlayers = max;
				        SingleServer.this.online = online;
				        Bukkit.getScheduler().runTask(plugin, new Runnable(){

							@Override
							public void run() {
								callUpdate(SingleServer.this);
								return;
							}});
				        
				        if(ip != null && port > -1){
				        	StatusResponse resp;
							try {
								resp = ServerListPing.fetchData(new InetSocketAddress(ip, port));
							} catch (IOException e) {
								return;
							}
							if(resp == null){
								return;
							}
							MOTD = resp.getDescription();
				        }
					return;
				}}, 0l, 10*20l);
		}
		
		public int getSlot(){
			return this.slot;
		}
		
		@Override
		public String getDisplayName() {
			return displayName;
		}
		@Override
		public List<String> getLore() {
			List<String> lore = new ArrayList<String>();
			lore.addAll(this.lore);
			lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
			if(isOnline){
				if(MOTD != null){
					lore.add(Colors.colorise(MOTD));
				}
				lore.add(ChatColor.WHITE+""+online+"/"+maxPlayers);
			}
			else {
				lore.add(offlineMsg);
			}
			return lore;
		}
		@Override
		public void onSelect(OptionClickEvent event, Player player) {
			player.sendMessage(ChatColor.GRAY+"Connecting to "+getDisplayName()+ChatColor.RESET+""+ChatColor.GRAY+"...");
			PlayerServerSender.sendToServer(player, bungeeName);
		}

		@Override
		public void update(IconMenu menu) {
			menu.setOption(slot, icon.clone(), getDisplayName(), getLore());
		}

		@Override
		public boolean isChild() {
			return child;
		}

		@Override
		public MultipleServer getParent() {
			return this.parent;
		}
	}
	
	public ServerSelector(GameBlade plugin){
		ServerSelector.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		initMenu();
	}
	
	private static void callUpdate(SelectableServer server){
		if(!server.isChild()){
			//It's on the main menu
			server.update(menu);
		}
		else if(server.getParent() != null){ //It's on a sub-menu
			server.update(server.getParent().subMenu);
		}
	}
	
	private void initMenu(){
		menu = new IconMenu(INVENTORY_TITLE, 18, this, plugin);
		
		SingleServer lobby = new SingleServer(new ItemStack(Material.BOOKSHELF), 0, 
				ChatColor.RED+"Main Lobby", 
				"hub1", "GB Lobby", 
				ChatColor.GRAY+""+ChatColor.ITALIC+"Go back to the main lobby");
		SingleServer mtasa = new SingleServer(new ItemStack(Material.IRON_SWORD), 2, 
				ChatColor.RED+"MineTheftAuto", 
				"mtasa", "GB MTASA", 
				ChatColor.GOLD+""+ChatColor.ITALIC+"Grand Theft Auto in Minecraft!");
		MultipleServer mk = new MultipleServer(new ItemStack(Material.MINECART), 3, 9,
				ChatColor.RED+"MarioKart", 
				"GB MarioKart", 
				ChatColor.GOLD+""+ChatColor.ITALIC+"MarioKart recreated in Minecraft!");
		SingleServer mk1 = new SingleServer(new ItemStack(Material.MINECART), 0, ChatColor.RED+"MK 1", "mk1", "GB MarioKart 1", mk, 
				ChatColor.GOLD+"Join MarioKart 1");
		mk1.setUseMOTD("bravo.gameblade.net", 4011);
		SingleServer mk2 = new SingleServer(new ItemStack(Material.MINECART), 1, ChatColor.RED+"MK 2", "mk2", "GB MarioKart 2", mk, 
				ChatColor.GOLD+"Join MarioKart 2");
		mk2.setUseMOTD("bravo.gameblade.net", 4012);
		SingleServer plots = new SingleServer(new ItemStack(Material.GRASS), 4, 
				ChatColor.GREEN+"Creative Plots", 
				"plots", "GB Plots", 
				ChatColor.AQUA+""+ChatColor.ITALIC+"Build what you want in your own plot!");
		SingleServer me = new SingleServer(new ItemStack(Material.IRON_BOOTS), 5, 
				ChatColor.RED+"Mirrors"+ChatColor.WHITE+" Edge", 
				"mirrorsedge1", "GB MirrorsEdge", 
				ChatColor.AQUA+""+ChatColor.ITALIC+"The famous parkour game meets",
				ChatColor.AQUA+""+ChatColor.ITALIC+"Minecraft, including a storyline!");
		SingleServer survival = new SingleServer(new ItemStack(Material.DIAMOND_SWORD), 6, 
				ChatColor.GOLD+"Survival", 
				"survival1", "GB Survival", 
				ChatColor.AQUA+""+ChatColor.ITALIC+"Survival Minecraft",
				ChatColor.AQUA+""+ChatColor.ITALIC+"Build, explore and battle with friends!");
		
		SingleServer ctw = new SingleServer(new ItemStack(Material.WOOL), 11, 
				ChatColor.BLUE+"Race For The Wool", 
				"ctw1", "GB CTW", 
				ChatColor.AQUA+""+ChatColor.ITALIC+"Race to be the first to collect the wool!");
		
		SingleServer events = new SingleServer(new ItemStack(Material.NETHER_STAR), 8, 
				ChatColor.GOLD+"Event Server", 
				"event1", "GB Event", 
				ChatColor.YELLOW+""+ChatColor.ITALIC+"Join a game/event that we normally don't have!");
		events.setOfflineMessage(ChatColor.RED+"There is currently no event running!");
		
	}
	
	public void open(final Player p){
		p.closeInventory();
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				menu.open(p);
		        p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
		        return;
			}}, 2l);
    }
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias,
                             String[] args) {
        if(!(sender instanceof Player)){
        	sender.sendMessage(ChatColor.RED+"Players only!");
        	return true;
        }
        
        Player player = ((Player)sender);
        open(player);
        return true;
    }
	

	@Override
	public void onOptionClick(OptionClickEvent event) {
		event.setWillClose(true);
		event.setWillDestroy(false);
		
		int slot = event.getPosition();
		for(SelectableServer ss:rootServers){
			if(ss.getSlot() == slot){
				ss.onSelect(event, event.getPlayer());
			}
		}
		
		return;
	}
    /*public String data = null;
    static GameBlade plugin;
    public static Inventory serverSelector = Bukkit.createInventory(null, 18, INVENTORY_TITLE);
    private ItemMeta immta;
    private ItemStack mta;
    private ItemMeta immk;
    private ItemStack mk;
    private ItemStack plots;
    private ItemStack mirrorsEdge;
    private ItemStack survival;
    private ItemStack GBSite;
    
    private IconMenu MKSelect;

    public ServerSelector(GameBlade instance)
    {
        plugin = instance;
        Bukkit.getPluginManager().registerEvents(this, instance);
        ItemStack vipservers = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta imvipservers = vipservers.getItemMeta();
        imvipservers.setDisplayName(ChatColor.YELLOW + "Event Server");
        imvipservers.setLore(Arrays.asList(new String[] { "" + ChatColor.RED + ChatColor.ITALIC + "There's currently no event running." }));
        vipservers.setItemMeta(imvipservers);
        
        MKSelect = new IconMenu(ChatColor.BLUE+"Select a server", 9, new IconMenu.OptionClickEventHandler() {
			
			@Override
			public void onOptionClick(OptionClickEvent event) {
				event.setWillClose(true);
				event.setWillDestroy(false);
				int pos = event.getPosition();
				ItemStack item = event.getInventory().getItem(pos);
				if(item == null){
					return;
				}
				String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()); //MK <No>
				if(!name.matches("MK (\\d+)")){
					return;
				}
				Pattern p = Pattern.compile("MK (\\d+)");
				Matcher matcher = p.matcher(name);
				
				int mkNo = 1;
				while(matcher.find()){
					//Should be one match
					mkNo = Integer.parseInt(matcher.group(1));
				}
				
				event.getPlayer().sendMessage(ChatColor.GRAY+"Connecting...");
				APIProvider.getAPI().sendToServer(event.getPlayer(), "mk"+mkNo);
				return;
			}
		}, GameBlade.plugin);
        
        Bukkit.getScheduler().runTaskTimerAsynchronously(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				//Update MK server list
				@SuppressWarnings("unused")
				int foundNo = 0;
				for(int i=4010;i<4020;i++){
					try {
						StatusResponse resp = ServerListPing.fetchData(new InetSocketAddress("37.59.50.130", i));
						if(resp == null){
							continue;
						}
						String MOTD = resp.getDescription();
						//System.out.println(MOTD);
						int playerCount = resp.getPlayers().getOnline();
						int playerMax = resp.getPlayers().getMax();
						
						int serverNumber = i - 4010;
						
						foundNo++;
						
						ItemStack[] selector = MKSelect.getOptions().clone();
						boolean found = false;
						
						for(int z=0;z<selector.length;z++){
							ItemStack it = selector[z];
							if(it == null){
								continue;
							}
							String name = it.getItemMeta().getDisplayName();
							if(ChatColor.stripColor(name).equalsIgnoreCase("MK "+serverNumber)){
								//Update it
								ItemMeta im = it.getItemMeta();
								im.setDisplayName(ChatColor.BLUE+"MK "+serverNumber);
								List<String> lore = new ArrayList<String>();
								lore.add(ChatColor.WHITE+Colors.colorise(MOTD));
								lore.add(ChatColor.WHITE+""+playerCount+"/"+playerMax);
								im.setLore(lore);
								it.setItemMeta(im);
								found = true;
							}
						}
						
						if(!found){ //Append it
							ItemStack it = new ItemStack(Material.MINECART);
							List<String> lore = new ArrayList<String>();
							lore.add(ChatColor.WHITE+Colors.colorise(MOTD));
							lore.add(ChatColor.WHITE+""+playerCount+"/"+playerMax);
							//Append it
							MKSelect.appendOption(it, ChatColor.BLUE+"MK "+serverNumber, lore);
						}
					} catch (Exception e) {
						// Server is offline
						e.printStackTrace();
					}
				}
				//GameBlade.logger.info("Found "+foundNo+" MK servers!");
				return;
			}}, 0l, 15*20l);

        mta = new ItemStack(Material.IRON_SWORD, 1);
        //mta.setDurability((short) 3);
        immta = mta.getItemMeta();
        immta.setDisplayName(ChatColor.RED + "MineTheftAuto");
        ArrayList<String> mtalore = new ArrayList<String>();
        mtalore.add("" + ChatColor.GOLD
                        + ChatColor.ITALIC
                        + "Grand Theft Auto in Minecraft!");
        mtalore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable(){

			@Override
			public void run() {
				 ArrayList<String> mtalore = new ArrayList<String>();
			        mtalore.add("" + ChatColor.GOLD
			                        + ChatColor.ITALIC
			                        + "Grand Theft Auto in Minecraft!");
			        mtalore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
			        int online = 0;
			        if(GameBlade.smApi != null){
			        	//ServerID = "GB MTASA 1";
			        	Server s = GameBlade.smApi.getServers().getServer("GB MTASA 1");
			        	if(s != null){ //Else it's not yet connected
			        		online = s.getOnlinePlayerCount();
			        	}
			        }
			        mtalore.add(ChatColor.WHITE + "" + online + ChatColor.BOLD + "/" + ChatColor.WHITE + "150");
			        immta.setLore(mtalore);
			        mta.setItemMeta(immta);
			        serverSelector.setItem(1, mta);
				return;
			}}, 10*20l, 10*20l);
        immta.setLore(mtalore);
        mta.setItemMeta(immta);
        
        plots = new ItemStack(Material.GRASS, 1);
        //mta.setDurability((short) 3);
        final ItemMeta implots = plots.getItemMeta();
        implots.setDisplayName(ChatColor.GREEN + "Creative Plots");
        ArrayList<String> plotslore = new ArrayList<String>();
        plotslore.addAll(Arrays.asList(new String[]{"" + ChatColor.AQUA
                        + ChatColor.ITALIC
                        + "Want to be creative? Build what",
                        "" + ChatColor.AQUA
                        + ChatColor.ITALIC
                        + "you want in your own plot!"}));
        plotslore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable(){

			@Override
			public void run() {
				 ArrayList<String> plotslore = new ArrayList<String>();
				 plotslore.addAll(Arrays.asList(new String[]{"" + ChatColor.AQUA
	                        + ChatColor.ITALIC
	                        + "Want to be creative? Build what",
	                        "" + ChatColor.AQUA
	                        + ChatColor.ITALIC
	                        + "you want in your own plot!"}));
			        plotslore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
			        int online = 0;
			        if(GameBlade.smApi != null){
			        	//ServerID = "GB Plots 1";
			        	List<String> ids = new ArrayList<String>(GameBlade.smApi.getServers().getServers().keySet());
			        	for(String s:ids){
			        		if(!s.toLowerCase().contains("plots") && !(s.toLowerCase().contains("plot"))){
			        			continue;
			        		}
			        		Server serv = GameBlade.smApi.getServers().getServer(s);
			        		if(serv != null){
			        			online += serv.getOnlinePlayerCount();
			        		}
			        	}
			        }
			        plotslore.add(ChatColor.WHITE + "" + online + ChatColor.BOLD + "/" + ChatColor.WHITE + "100");
			        implots.setLore(plotslore);
			        plots.setItemMeta(implots);
			        serverSelector.setItem(3, plots);
				return;
			}}, 10*20l, 10*20l);
        implots.setLore(plotslore);
        plots.setItemMeta(implots);
        
        survival = new ItemStack(Material.DIAMOND_SWORD, 1);
        //mta.setDurability((short) 3);
        final ItemMeta imsurvival = survival.getItemMeta();
        imsurvival.setDisplayName(ChatColor.GOLD + "Survival");
        final ArrayList<String> survivallore = new ArrayList<String>();
        survivallore.addAll(Arrays.asList(new String[]{"" + ChatColor.AQUA
                        + ChatColor.ITALIC
                        + "Survival Minecraft",
                        "" + ChatColor.AQUA
                        + ChatColor.ITALIC
                        + "Build, Explore and battle with friends"}));
        survivallore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable(){

			@Override
			public void run() {
				 ArrayList<String> slore = new ArrayList<String>(survivallore);
			        int online = 0;
			        if(GameBlade.smApi != null){
			        	//ServerID = "GB Survival 1";
			        	List<String> ids = new ArrayList<String>(GameBlade.smApi.getServers().getServers().keySet());
			        	for(String s:ids){
			        		if(!s.toLowerCase().contains("survival") && !(s.toLowerCase().contains("survivals"))){
			        			continue;
			        		}
			        		Server serv = GameBlade.smApi.getServers().getServer(s);
			        		if(serv != null){
			        			online += serv.getOnlinePlayerCount();
			        		}
			        	}
			        }
			        slore.add(ChatColor.WHITE + "" + online + ChatColor.BOLD + "/" + ChatColor.WHITE + "100");
			        imsurvival.setLore(slore);
			        survival.setItemMeta(imsurvival);
			        serverSelector.setItem(5, survival);
				return;
			}}, 10*20l, 10*20l);
        imsurvival.setLore(survivallore);
        survival.setItemMeta(imsurvival);
        
        mirrorsEdge = new ItemStack(Material.IRON_BOOTS, 1);
        //mta.setDurability((short) 3);
        final ItemMeta imme = mirrorsEdge.getItemMeta();
        imme.setDisplayName(ChatColor.RED + "Mirror's" + ChatColor.WHITE + " Edge");
        ArrayList<String> melore = new ArrayList<String>();
        melore.addAll(Arrays.asList(new String[]{"" + ChatColor.AQUA
                        + ChatColor.ITALIC
                        + "The famous parkour game meets",
                        "" + ChatColor.AQUA
                        + ChatColor.ITALIC
                        + "Minecraft, including a storyline!"}));
        melore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable(){

			@Override
			public void run() {
				 ArrayList<String> melore = new ArrayList<String>();
				 melore.addAll(Arrays.asList(new String[]{"" + ChatColor.AQUA
	                        + ChatColor.ITALIC
	                        + "The famous parkour game meets",
	                        "" + ChatColor.AQUA
	                        + ChatColor.ITALIC
	                        + "Minecraft, including a storyline!"}));
			        melore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
			        int online = 0;
			        if(GameBlade.smApi != null){
			        	//ServerID = "GB Plots 1";
			        	List<String> ids = new ArrayList<String>(GameBlade.smApi.getServers().getServers().keySet());
			        	for(String s:ids){
			        		if(!s.toLowerCase().contains("mirrorsedge") && !(s.toLowerCase().contains("mirrors edge"))){
			        			continue;
			        		}
			        		Server serv = GameBlade.smApi.getServers().getServer(s);
			        		if(serv != null){
			        			online += serv.getOnlinePlayerCount();
			        		}
			        	}
			        }
			        melore.add(ChatColor.WHITE + "" + online + ChatColor.BOLD + "/" + ChatColor.WHITE + "50");
			        imme.setLore(melore);
			        mirrorsEdge.setItemMeta(imme);
			        serverSelector.setItem(4, mirrorsEdge);
				return;
			}}, 10*20l, 10*20l);
        imme.setLore(melore);
        mirrorsEdge.setItemMeta(imme);

        mk = new ItemStack(Material.MINECART, 1);
        immk = mk.getItemMeta();
        immk.setDisplayName(ChatColor.RED + "MarioKart");
        ArrayList<String> mklore = new ArrayList<String>();
        mklore.add("" + ChatColor.GOLD
                        + ChatColor.ITALIC
                        + "Race like Mario in Mariokart for Minecraft!");
        mklore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable(){

			@Override
			public void run() {
				ArrayList<String> mklore = new ArrayList<String>();
		        mklore.add("" + ChatColor.GOLD
		                        + ChatColor.ITALIC
		                        + "Race like Mario in Mariokart for Minecraft!");
		        mklore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
			        int online = 0;
			        if(GameBlade.smApi != null){
			        	//ServerID = "GB MarioKart 1";
			        	List<String> ids = new ArrayList<String>(GameBlade.smApi.getServers().getServers().keySet());
			        	for(String s:ids){
			        		if(!s.toLowerCase().contains("mariokart") && !(s.toLowerCase().contains("mario kart"))){
			        			continue;
			        		}
			        		Server serv = GameBlade.smApi.getServers().getServer(s);
			        		if(serv != null){
			        			online += serv.getOnlinePlayerCount();
			        		}
			        	}
			        }
			        mklore.add(ChatColor.WHITE + "" + online + ChatColor.BOLD + "/" + ChatColor.WHITE + "50");
			        immk.setLore(mklore);
			        mk.setItemMeta(immk);
			        serverSelector.setItem(2, mk);
				return;
			}}, 10*20l, 10*20l);
        immk.setLore(mklore);
        mk.setItemMeta(immk);

        ItemStack lobby = new ItemStack(Material.BOOKSHELF, 1);
        ItemMeta imlobby = lobby.getItemMeta();
        imlobby.setDisplayName(ChatColor.RED + "Main Lobby");
        imlobby.setLore(Arrays.asList(new String[] { "" + ChatColor.GRAY + ChatColor.ITALIC + "Go back to the Main Lobby" }));
        lobby.setItemMeta(imlobby);

        ItemStack wip = new ItemStack(Material.CLAY_BRICK, 1);
        ItemMeta imwip = wip.getItemMeta();
        imwip.setDisplayName(ChatColor.RED + "Work in progress!");
        imwip.setLore(Arrays.asList(new String[] { "" + ChatColor.GRAY + ChatColor.ITALIC + "What's coming here!?" }));
        wip.setItemMeta(imwip);
        
        GBSite = new ItemStack(Material.SKULL_ITEM, 1);
        CustomPlayerHeads.setSkullWithNonPlayerProfile(Config.GBSkullURL.getValue(), true, GBSite);
        ItemMeta im = GBSite.getItemMeta();
        im.setDisplayName(ChatColor.GOLD+"Game"+ChatColor.BLUE+"Blade"+ChatColor.WHITE+" website");
        im.setLore(Arrays.asList(new String[]{ChatColor.GRAY+"www.gameblade.net", ChatColor.GRAY+"Click to visit site"}));
        GBSite.setItemMeta(im);
        
        serverSelector.setItem(8, vipservers);
        serverSelector.setItem(1, mta);
        serverSelector.setItem(2, mk);
        serverSelector.setItem(3, plots);
        serverSelector.setItem(4, mirrorsEdge);
        serverSelector.setItem(5, survival);
        serverSelector.setItem(6, collectTheWool);
        serverSelector.setItem(7, wip);
        serverSelector.setItem(0, lobby);

        serverSelector.setItem(10, wip);
        serverSelector.setItem(11, wip);
        serverSelector.setItem(12, wip);
        serverSelector.setItem(13, wip);
        serverSelector.setItem(14, wip);
        serverSelector.setItem(15, wip);
        serverSelector.setItem(16, wip);
    }
    
    public void open(Player p){
    	p.openInventory(serverSelector);
        p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player)event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        ChatColor.stripColor(inventory.getTitle()).equals(ChatColor.stripColor(INVENTORY_TITLE));
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        if(clicked == null){
            return;
        }

        try {
        	if(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("GameBlade website")){
        		p.sendMessage("http://www.gameblade.net <- Click to visit our website!");
        		return;
        	}
            if ((clicked.getType() == Material.IRON_SWORD) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "MineTheftAuto"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF("mtasa");
                }
                catch (IOException localIOException1) {
                }
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
            if ((clicked.getType() == Material.MINECART) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "MarioKart"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
                //Open MK server select menu
                MKSelect.open(p);
            }
            if ((clicked.getType() == Material.GRASS) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Creative Plots"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF("plots");
                    }
                    catch (IOException localIOException1) {
                    }
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
            if(clicked.getType().equals(Material.DIAMOND_SWORD)
            		&& clicked.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Survival")){
            	event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF("survival1");
                    }
                    catch (IOException localIOException1) {
                    }
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
            if(clicked.getType().equals(Material.IRON_BOOTS) && clicked.getItemMeta().getDisplayName() != null
            		&& clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Mirror's" + ChatColor.WHITE + " Edge")){
            	 event.setCancelled(true);
                 p.closeInventory();
                 p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
                     try {
                         out.writeUTF("Connect");
                         out.writeUTF("mirrorsedge1");
                     }
                     catch (IOException localIOException1) {
                     }
                     p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
            if ((clicked.getType() == Material.NETHER_STAR) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Event Server"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
                p.sendMessage(ChatColor.GOLD + "Game" + ChatColor.BLUE + "Blade" + ChatColor.BLACK + "» " + ChatColor.RED + "There's currently no event running!");
            }
            if ((clicked.getType() == Material.BOOKSHELF) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Main Lobby"))) {
            	event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 10.0F);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF("hub1");
                }
                catch (IOException localIOException1) {
                }
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
            if ((clicked.getType() == Material.CLAY_BRICK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Work in progress!"))) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.GOLD + "Game" + ChatColor.BLUE + "Blade" + ChatColor.BLACK + "» " + ChatColor.RED + "There'll be a server here, soon!");
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                p.closeInventory();
            }
        } catch (Exception e) {
            //They are building, not menu selecting...
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias,
                             String[] args) {
        if(!(sender instanceof Player)){
        	sender.sendMessage(ChatColor.RED+"Players only!");
        	return true;
        }
        
        Player player = ((Player)sender);
        open(player);
        return true;
    }*/
}
