package im.mta.coremanager.modmenu;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class AdminMenu implements Listener, CommandExecutor {

    public static GameBlade plugin;

    public boolean chatDisabled = false;

    public static ArrayList<String> vanishArray = new ArrayList<String>();
    public static ArrayList<String> staffModeArray = new ArrayList<String>();

    public Inventory adminMenu = Bukkit.createInventory(null, 9, ChatColor.RED + "Admin Menu");
    public Inventory modSettingsMenu = Bukkit.createInventory(null, 9, ChatColor.RED + "Your Settings");
    public Inventory serverSettingsMenu = Bukkit.createInventory(null, 9, ChatColor.RED + "Server Settings");
    public Inventory serverActionsMenu = Bukkit.createInventory(null, 9, ChatColor.RED + "Server Actions");

    public void createDisplay(Player p) {

        ItemStack serverStats = new ItemStack(Material.PAPER, 1);
        ItemMeta serverStatsim = serverStats.getItemMeta();
        serverStatsim.setDisplayName(ChatColor.RED + "Server Stats");
        ArrayList<String> serverStatslore = new ArrayList<String>();
        serverStatslore.add(ChatColor.GRAY + "Online Players: " + ChatColor.WHITE + GameBlade.serverInfo.getPlayerCount());
        serverStatslore.add(ChatColor.GRAY + "Total RAM: " + ChatColor.WHITE + GameBlade.serverInfo.getMaxRam());
        serverStatslore.add(ChatColor.GRAY + "RAM in Use: " + ChatColor.WHITE + GameBlade.serverInfo.getUsedRam());
        serverStatslore.add(ChatColor.GRAY + "Free RAM: " + ChatColor.WHITE + (GameBlade.serverInfo.getMaxRam()-GameBlade.serverInfo.getUsedRam()));
        serverStatslore.add(ChatColor.GRAY + "Resource Score %: " + ChatColor.WHITE + (GameBlade.serverInfo.getResourceScore()));
        serverStatslore.add(ChatColor.GRAY + "TPS: " + ChatColor.WHITE + GameBlade.serverInfo.getTPS());
        serverStatsim.setLore(serverStatslore);
        serverStats.setItemMeta(serverStatsim);

        ItemStack serverSettings = new ItemStack(Material.BOOK, 1);
        ItemMeta serverSettingsim = serverSettings.getItemMeta();
        serverSettingsim.setDisplayName(ChatColor.RED + "Server Settings");
        serverSettings.setItemMeta(serverSettingsim);

        ItemStack serverActions = new ItemStack(Material.BOOK, 1);
        ItemMeta serverActionsim = serverActions.getItemMeta();
        serverActionsim.setDisplayName(ChatColor.RED + "Server Actions");
        serverActions.setItemMeta(serverActionsim);

        ItemStack yourSettings = new ItemStack(Material.BOOK, 1);
        ItemMeta yourSettingsim = yourSettings.getItemMeta();
        yourSettingsim.setDisplayName(ChatColor.RED + "Your Settings");
        yourSettings.setItemMeta(yourSettingsim);

        ItemStack exit = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta exitim = exit.getItemMeta();
        exitim.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exitim);

        adminMenu.setItem(0, serverStats);
        adminMenu.setItem(1, serverSettings);
        adminMenu.setItem(2, serverActions);
        adminMenu.setItem(4, yourSettings);
        adminMenu.setItem(8, exit);
    }

    public void createServerActionsMenu(Player p) {

        short enabled = 10;
        short disabled = 8;

        ItemStack clearchat = new ItemStack(Material.BRICK, 1);
        ItemMeta clearchatim = clearchat.getItemMeta();
        clearchatim.setDisplayName(ChatColor.RED + "Clear Chat");
        ArrayList<String> clearchatlore = new ArrayList<String>();
        clearchatlore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Clear the chat");
        clearchatim.setLore(clearchatlore);
        clearchat.setItemMeta(clearchatim);

        ItemStack tpall = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta tpallim = tpall.getItemMeta();
        tpallim.setDisplayName(ChatColor.RED + "Teleport All");
        ArrayList<String> tpalllore = new ArrayList<String>();
        tpalllore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Teleports all players to you");
        tpallim.setLore(tpalllore);
        tpall.setItemMeta(tpallim);

        ItemStack exit = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta exitim = exit.getItemMeta();
        exitim.setDisplayName(ChatColor.RED + "Go back");
        exit.setItemMeta(exitim);

        serverActionsMenu.setItem(0, tpall);
        serverActionsMenu.setItem(1, clearchat);
        serverActionsMenu.setItem(8, exit);
    }

    public void createServerSettingsMenu(Player p) {

        short enabled = 10;
        short disabled = 8;

        ItemStack chat = new ItemStack(Material.INK_SACK, 1);
        if(!chatDisabled) chat.setDurability(disabled);
        else chat.setDurability(enabled);
        ItemMeta chatim = chat.getItemMeta();
        chatim.setDisplayName(ChatColor.RED + "Disable Chat");
        ArrayList<String> chatlore = new ArrayList<String>();
        chatlore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to toggle on/off server chat");
        chatim.setLore(chatlore);
        chat.setItemMeta(chatim);

        ItemStack exit = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta exitim = exit.getItemMeta();
        exitim.setDisplayName(ChatColor.RED + "Go back");
        exit.setItemMeta(exitim);

        serverSettingsMenu.setItem(0, chat);
        serverSettingsMenu.setItem(8, exit);
    }
    public void createModSettingsMenu(Player p) {

        short enabled = 10;
        short disabled = 8;

        ItemStack vanish = new ItemStack(Material.INK_SACK, 1);
        if(!vanishArray.contains(p.getName())) vanish.setDurability(disabled);
        else vanish.setDurability(enabled);
        ItemMeta vanishim = vanish.getItemMeta();
        vanishim.setDisplayName(ChatColor.RED + "Toggle Vanish");
        ArrayList<String> vanishLore = new ArrayList<String>();
        vanishLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to toggle vanish mode");
        vanishim.setLore(vanishLore);
        vanish.setItemMeta(vanishim);

        ItemStack staffMode = new ItemStack(Material.INK_SACK, 1);
        if(!staffModeArray.contains(p.getName())) staffMode.setDurability(disabled);
        else staffMode.setDurability(enabled);
        ItemMeta staffModeim = vanish.getItemMeta();
        staffModeim.setDisplayName(ChatColor.RED + "Staff Mode");
        ArrayList<String> staffModeLore = new ArrayList<String>();
        staffModeLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to toggle staff mode");
        staffModeim.setLore(staffModeLore);
        staffMode.setItemMeta(staffModeim);

        ItemStack exit = new ItemStack(Material.IRON_DOOR, 1);
        ItemMeta exitim = exit.getItemMeta();
        exitim.setDisplayName(ChatColor.RED + "Go back");
        exit.setItemMeta(exitim);

        modSettingsMenu.setItem(0, vanish);
        modSettingsMenu.setItem(1, staffMode);
        modSettingsMenu.setItem(8, exit);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player)e.getWhoClicked();
        if(p.getInventory() == adminMenu) e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if(p.getInventory() == adminMenu) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        Inventory inventory = e.getInventory();
        try {
            if (clicked == null) {
                return;
            }
            if ((clicked.getType() == Material.PAPER) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Server Stats"))) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            }
            if ((clicked.getType() == Material.BOOK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Server Settings"))) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                createServerSettingsMenu(p);
                p.openInventory(serverSettingsMenu);
            }
            if ((clicked.getType() == Material.BOOK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Server Actions"))) {
                e.setCancelled(true);
                p.updateInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                createServerActionsMenu(p);
                p.openInventory(serverActionsMenu);
            }
            if ((clicked.getType() == Material.BOOK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Your Settings"))) {
                e.setCancelled(true);
                p.updateInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                createModSettingsMenu(p);
                p.openInventory(modSettingsMenu);
            }
            if ((clicked.getType() == Material.IRON_DOOR) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Exit"))) {
                e.setCancelled(true);
                p.updateInventory();
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            }
            if ((clicked.getType() == Material.IRON_DOOR) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Go back"))) {
                e.setCancelled(true);
                createDisplay(p);
                p.openInventory(adminMenu);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            }
            if ((clicked.getType() == Material.BRICK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Clear Chat"))) {
                e.setCancelled(true);
                if(p.hasPermission("admin.chat.clear")) {
                    for (int i = 0; i < 50; i++) {
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(ChatColor.RED + "Chat cleared by a moderator.");
                }
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            }
            if ((clicked.getType() == Material.BLAZE_ROD) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Teleport All"))) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                if(p.hasPermission("admin.tpall")) {
                    p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1F, 1F);
                    for( Player players : Bukkit.getOnlinePlayers()) {
                        players.teleport(new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ()));
                    }
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "ALERT " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "All players have been teleported to " + ChatColor.AQUA + p.getName() + ChatColor.RED + "!");
                }
                else {
                    p.closeInventory();
                    p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
                }
            }
            if ((clicked.getType() == Material.INK_SACK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Disable Chat"))) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                if(p.hasPermission("admin.chat")) {
                    if(chatDisabled) {
                        chatDisabled = false;
                        createDisplay(p);
                        p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1F, 1F);
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "ALERT " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "Chat has been enabled by " + ChatColor.AQUA + p.getName() + ChatColor.RED + "! Everyone is able to chat again.");
                    }
                    else {
                        chatDisabled = true;
                        createDisplay(p);
                        p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1F, 1F);
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "ALERT " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "Chat has been disabled by " + ChatColor.AQUA + p.getName() + ChatColor.RED + "! Currently only staff members are allowed to chat.");
                    }
                }
                else {
                    p.closeInventory();
                    p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
                }
            }
            if ((clicked.getType() == Material.INK_SACK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Toggle Vanish"))) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                if(p.hasPermission("admin.vanish")) {
                    if(vanishArray.contains(p.getName())) {
                        vanishArray.remove(p.getName());
                        p.setCanPickupItems(true);
                        createDisplay(p);
                        for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                            if (!allPlayers.getName().equalsIgnoreCase(p.getName())) {
                                if (!allPlayers.hasPermission("admin.main")) {
                                    allPlayers.showPlayer(p);
                                }
                            }
                        }
                        for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                            if (allPlayers.hasPermission("admin.main")) {
                                allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " is no longer vanished!");
                            }
                        }
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);
                        p.sendMessage(ChatColor.RED + "[ADMIN ALERT] You're no longer vanished!");
                    }
                    else {
                        vanishArray.add(p.getName());
                        p.setCanPickupItems(false);
                        createDisplay(p);
                        for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                            if (!allPlayers.getName().equalsIgnoreCase(p.getName())) {
                                if (!allPlayers.hasPermission("admin.main")) {
                                    allPlayers.hidePlayer(p);
                                }
                            }
                        }
                        for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                            if (allPlayers.hasPermission("admin.main")) {
                                allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " is now vanished!");
                            }
                        }
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);
                        p.sendMessage(ChatColor.RED + "[ADMIN ALERT] You're now vanished!");
                    }
                }
                else {
                    p.closeInventory();
                    p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
                }
            }
            if ((clicked.getType() == Material.INK_SACK) &&
                    (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Staff Mode"))) {
                e.setCancelled(true);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                if(p.hasPermission("admin.staffmode")) {
                    if(staffModeArray.contains(p.getName())) {
                        staffModeArray.remove(p.getName());
                        p.setGameMode(GameMode.ADVENTURE);
                        p.setAllowFlight(false);
                        p.playSound(p.getLocation(), Sound.FIREWORK_LARGE_BLAST2, 1F, 1F);
                        createDisplay(p);
                        for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                            if (allPlayers.hasPermission("admin.main")) {
                                allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " is no longer in staff mode!");
                            }
                        }
                        p.sendMessage(ChatColor.RED + "[ADMIN ALERT] You're no longer in staff mode!");
                    }
                    else {
                        staffModeArray.add(p.getName());
                        p.setGameMode(GameMode.CREATIVE);
                        p.setAllowFlight(true);
                        p.playSound(p.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1F, 1F);
                        createDisplay(p);
                        for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                            if (allPlayers.hasPermission("admin.main")) {
                                allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " is now in staff mode!");
                            }
                        }
                        p.sendMessage(ChatColor.RED + "[ADMIN ALERT] You're now in staff mode!");
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You're not allowed to use this!");
                }
            }
        } catch (Exception e1) {
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String Label, String args[]) {
        if (sender instanceof Player) {
            if (sender.hasPermission("admin.openmenu")) {
                ((Player) sender).openInventory(adminMenu);
                createDisplay((Player) sender);
            }
            else {
                sender.sendMessage("Unknown command. Type \"/help\" for help.");
            }
        }
        else {
            sender.sendMessage("No.");
        }
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            if (!staff.getName().equalsIgnoreCase(p.getName())) {
                if (vanishArray.contains(staff.getName())) {
                    p.hidePlayer(staff);
                }
            }
        }
    }
}
