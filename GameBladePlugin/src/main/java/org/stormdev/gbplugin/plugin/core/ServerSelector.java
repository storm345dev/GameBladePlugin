package org.stormdev.gbplugin.plugin.core;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.stormdev.gbapi.core.APIProvider;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.storm.skulls.CustomPlayerHeads;
import org.stormdev.gbplugin.plugin.server.ping.ServerListPing;
import org.stormdev.gbplugin.plugin.server.ping.ServerListPing.StatusResponse;
import org.stormdev.gbplugin.plugin.utils.Colors;
import org.stormdev.servermanager.api.messaging.Server;

public class ServerSelector implements CommandExecutor, Listener
{
	private static final String INVENTORY_TITLE = ChatColor.RED + "Choose a server";
    public String data = null;
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
			        serverSelector.setItem(2, mta);
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
			        serverSelector.setItem(4, plots);
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
			        serverSelector.setItem(6, survival);
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
			        serverSelector.setItem(5, mirrorsEdge);
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
			        serverSelector.setItem(3, mk);
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
        serverSelector.setItem(2, mta);
        serverSelector.setItem(3, mk);
        serverSelector.setItem(4, plots);
        serverSelector.setItem(5, mirrorsEdge);
        serverSelector.setItem(6, survival);
        serverSelector.setItem(0, lobby);

        serverSelector.setItem(11, wip);
        serverSelector.setItem(12, wip);
        serverSelector.setItem(13, wip);
        serverSelector.setItem(14, wip);
        serverSelector.setItem(15, wip);
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
    }
}
