package im.mta.coremanager.modmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class ServerActionsMenu {
    public static IconMenu serverActionsMenu;

    static {
        serverActionsMenu = new IconMenu(ChatColor.RED + "Main " + ChatColor.DARK_GRAY + "»" + ChatColor.RED + " Server Actions", 9, new OptionClickEventHandler(){

            @Override
            public void onOptionClick(OptionClickEvent event) {
                event.setWillClose(false);
                event.setWillDestroy(false);
                onClick(event);
                return;
            }}, GameBlade.plugin);

        serverActionsMenu.setOption(0,
                new ItemStack(Material.BRICK),
                ChatColor.RED + "Clear Chat",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Clears the chat");
        serverActionsMenu.setOption(1,
                new ItemStack(Material.BLAZE_ROD),
                ChatColor.RED + "Teleport All",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Teleports all players to you");
        serverActionsMenu.setOption(2,
                new ItemStack(Material.WATER_BUCKET),
                ChatColor.RED + "Toggle Downfall",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Toggles Downfall");
        serverActionsMenu.setOption(3,
                new ItemStack(Material.WATCH),
                ChatColor.RED + "Toggle Time",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Toggles Day/Night");
        serverActionsMenu.setOption(8,
                new ItemStack(Material.IRON_DOOR),
                ChatColor.RED + "Go back");
    }

    public static void open(Player player){
        serverActionsMenu.open(player);
    }

    private static OptionClickEvent onClick(OptionClickEvent e){
        //Changeclass
        int pos = e.getPosition();
        final Player p = e.getPlayer();
        if(pos == 0){
            if(p.hasPermission("admin.chat.clear")) {
                for (int i = 0; i < 50; i++) {
                    Bukkit.broadcastMessage("");
                }
                Bukkit.broadcastMessage(ChatColor.RED + "Chat cleared by a moderator.");
            }
            else {
                e.setWillClose(true);
                p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
            }
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        }
        if(pos == 1){
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            if(p.hasPermission("admin.tpall")) {
                p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1F, 1F);
                for( Player players : Bukkit.getOnlinePlayers()) {
                    players.teleport(new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ()));
                }
                Bukkit.broadcastMessage(ChatColor.YELLOW + "ALERT " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "All players have been teleported to " + ChatColor.AQUA + p.getName() + ChatColor.RED + "!");
            }
            else {
                e.setWillClose(true);
                p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
            }
        }
        if(pos == 2){
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            if(p.hasPermission("admin.weather")) {
                p.getWorld().setStorm(false);
                for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                    if (allPlayers.hasPermission("admin.main")) {
                        allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " toggled downfall to false!");
                    }
                }
            }
            else {
                e.setWillClose(true);
                p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
            }
        }
        if(pos == 3){
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            if(p.hasPermission("admin.time")) {
            	long time = p.getWorld().getTime();
                if(time > 13000) {
                    p.getWorld().setTime(0L);
                    for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                        if (allPlayers.hasPermission("admin.main")) {
                            allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " changed time to day!");
                        }
                    }
                }
                else {
                    p.getWorld().setTime(13000L);
                    for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                        if (allPlayers.hasPermission("admin.main")) {
                            allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " changed time to night!");
                        }
                    }
                }
            }
            else {
                e.setWillClose(true);
                p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
            }
        }
        else if(pos == 8){
            e.setWillClose(true);

            Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable() {
                @Override
                public void run() {
                    MainMenu.open(p);
                }
            }, 2l);
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        }
        else{
            return e;
        }

        return e;
    }
}
