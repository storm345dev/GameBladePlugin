package org.stormdev.gbplugin.plugin.core;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.stormdev.servermanager.api.messaging.Server;

public class ServerSelector implements CommandExecutor, Listener
{
	private static final String INVENTORY_TITLE = ChatColor.GREEN + "Choose a server";
    public String data = null;
    static GameBlade plugin;
    public static Inventory serverSelector = Bukkit.createInventory(null, 18, INVENTORY_TITLE);
    private ItemMeta immta;
    private ItemStack mta;
    private ItemMeta immk;
    private ItemStack mk;
    private ItemStack plots;

    public ServerSelector(GameBlade instance)
    {
        plugin = instance;
        Bukkit.getPluginManager().registerEvents(this, instance);
        ItemStack vipservers = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta imvipservers = vipservers.getItemMeta();
        imvipservers.setDisplayName(ChatColor.YELLOW + "VIP Servers");
        imvipservers.setLore(Arrays.asList(new String[] { "" + ChatColor.GRAY + ChatColor.ITALIC + "Special servers for our VIPS!" }));
        vipservers.setItemMeta(imvipservers);

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
        implots.setDisplayName(ChatColor.GREEN + "Plots");
        ArrayList<String> plotslore = new ArrayList<String>();
        plotslore.add("" + ChatColor.AQUA
                        + ChatColor.ITALIC
                        + "Minecraft Plots!");
        plotslore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------");
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable(){

			@Override
			public void run() {
				 ArrayList<String> plotslore = new ArrayList<String>();
			        plotslore.add("" + ChatColor.AQUA
			                        + ChatColor.ITALIC
			                        + "Minecraft Plots!");
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

        ItemStack close = new ItemStack(Material.REDSTONE, 1);
        ItemMeta imclose = close.getItemMeta();
        imclose.setDisplayName(ChatColor.RED + "Exit");
        imclose.setLore(Arrays.asList(new String[] { "" + ChatColor.GRAY + ChatColor.ITALIC + "Close this screen" }));
        close.setItemMeta(imclose);

        ItemStack wip = new ItemStack(Material.CLAY_BRICK, 1);
        ItemMeta imwip = wip.getItemMeta();
        imwip.setDisplayName(ChatColor.RED + "Work in progress!");
        imwip.setLore(Arrays.asList(new String[] { "" + ChatColor.GRAY + ChatColor.ITALIC + "What's coming here!?" }));
        wip.setItemMeta(imwip);

        serverSelector.setItem(8, vipservers);
        serverSelector.setItem(2, mta);
        serverSelector.setItem(3, mk);
        serverSelector.setItem(4, plots);
        serverSelector.setItem(0, close);

        serverSelector.setItem(5, wip);
        serverSelector.setItem(6, wip);
        serverSelector.setItem(11, wip);
        serverSelector.setItem(12, wip);
        serverSelector.setItem(13, wip);
        serverSelector.setItem(14, wip);
        serverSelector.setItem(15, wip);
    }
    
    public void open(Player p){
    	p.openInventory(serverSelector);
        p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
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
            if ((clicked.getType() == Material.IRON_SWORD) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "MineTheftAuto"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
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
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF("mklobby1");
                    }
                    catch (IOException localIOException1) {
                    }
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
            if ((clicked.getType() == Material.GRASS) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Plots"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF("plots");
                    }
                    catch (IOException localIOException1) {
                    }
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
            if ((clicked.getType() == Material.NETHER_STAR) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "VIP Servers"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                p.sendMessage(ChatColor.YELLOW + "Lobby " + ChatColor.BLACK + "Â» " + ChatColor.RED + "VIP Servers are coming very soon(tm)!");
            }
            if ((clicked.getType() == Material.REDSTONE) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Exit"))) {
                event.setCancelled(true);
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            }
            if ((clicked.getType() == Material.CLAY_BRICK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Work in progress!"))) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.YELLOW + "Lobby " + ChatColor.BLACK + "Â» " + ChatColor.RED + "There'll be a server here, soon!");
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