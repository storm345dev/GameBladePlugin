package im.mta.coremanager.modmenu;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.gbplugin.plugin.utils.MetaValue;

public class ModSettingsMenu {

    private IconMenu modSettingsMenu;

    public static ArrayList<String> vanishArray = new ArrayList<String>();
    public static ArrayList<String> staffModeArray = new ArrayList<String>();

    public short vanishStatus(Player p) {
        short status;
        if(vanishArray.contains(p.getName())) status = 10;
        else status = 8;

        return status;
    }

    public short staffModeStatus(Player p) {
        short status;
        if(staffModeArray.contains(p.getName())) status = 10;
        else status = 8;

        return status;
    }

    public ModSettingsMenu(Player p){
        modSettingsMenu = new IconMenu(ChatColor.RED + "Main " + ChatColor.DARK_GRAY + "»" + ChatColor.RED + " Your Settings", 9, new OptionClickEventHandler(){

            @Override
            public void onOptionClick(OptionClickEvent event) {
                event.setWillClose(false);
                event.setWillDestroy(false);
                event = onClick(event);
                return;
            }}, GameBlade.plugin);

        modSettingsMenu.setOption(0,
                new ItemStack(Material.INK_SACK, 1, vanishStatus(p)),
                ChatColor.RED + "Toggle Vanish",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to toggle vanish mode");
        modSettingsMenu.setOption(1,
                new ItemStack(Material.INK_SACK, 1, staffModeStatus(p)),
                ChatColor.RED + "Staff Mode",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to toggle staff mode");
        modSettingsMenu.setOption(8,
                new ItemStack(Material.IRON_DOOR),
                ChatColor.RED + "Go back");
    }

    public void open(Player player){
        modSettingsMenu.open(player);
    }

    private OptionClickEvent onClick(OptionClickEvent e){
        //Changeclass
        int pos = e.getPosition();
        final Player p = e.getPlayer();
        if(pos == 0){
            if(p.hasPermission("admin.vanish")) {
                if(vanishArray.contains(p.getName())) {
                    vanishArray.remove(p.getName());
                    p.setCanPickupItems(true);
                    e.setWillClose(true);
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
                    e.setWillClose(true);
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
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        }
        else if(pos == 1){
            if(p.hasPermission("admin.staffmode")) {
                if(staffModeArray.contains(p.getName())) {
                    staffModeArray.remove(p.getName());
                    p.setGameMode(GameMode.ADVENTURE);
                    p.setHealthScaled(false);
                    p.removeMetadata("invincible", GameBlade.plugin);
                    p.setAllowFlight(false);
                    p.playSound(p.getLocation(), Sound.FIREWORK_LARGE_BLAST2, 1F, 1F);
                    e.setWillClose(true);
                    for (Player allPlayers : Bukkit.getServer().getOnlinePlayers()) {
                        if (allPlayers.hasPermission("admin.main")) {
                            allPlayers.sendMessage(ChatColor.RED + "[ADMIN ALERT] " + p.getName() + " is no longer in staff mode!");
                        }
                    }
                    p.sendMessage(ChatColor.RED + "[ADMIN ALERT] You're no longer in staff mode!");
                }
                else {
                    staffModeArray.add(p.getName());
                    p.setAllowFlight(true);
                    p.setHealthScaled(true);
                    p.setHealthScale(100);
                    p.setMetadata("invincible", new MetaValue(true, GameBlade.plugin));
                    p.playSound(p.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1F, 1F);
                    e.setWillClose(true);
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
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        }
        else if(pos == 8){
            e.setWillClose(true);
            e.setWillDestroy(true);

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
