package im.mta.coremanager.modmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class ServerSettingsMenu {

    private static IconMenu serverSettingsMenu;

    public static boolean chatDisabled = false;

    static {
        serverSettingsMenu = new IconMenu(ChatColor.RED + "Main " + ChatColor.DARK_GRAY + "»" + ChatColor.RED + " Server Settings", 9, new OptionClickEventHandler(){

            @Override
            public void onOptionClick(OptionClickEvent event) {
                event.setWillClose(false);
                event.setWillDestroy(false);
                onClick(event);
                return;
            }}, GameBlade.plugin);

        short status;
        if(chatDisabled) status = 10;
        else status = 8;

        serverSettingsMenu.setOption(0,
                new ItemStack(Material.INK_SACK, 1, status),
                ChatColor.RED + "Disable Chat",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to toggle on/off server chat");
        serverSettingsMenu.setOption(8,
                new ItemStack(Material.IRON_DOOR),
                ChatColor.RED + "Go back");
    }

    private static void updateStatus(){
        short status;
        if(chatDisabled) status = 10;
        else status = 8;

        //Bukkit.broadcastMessage("Status: "+status);

        serverSettingsMenu.setOption(0,
                new ItemStack(Material.INK_SACK, 1, status),
                ChatColor.RED + "Disable Chat",
                ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to toggle on/off server chat");
    }

    public static void open(Player player){
        updateStatus();
        serverSettingsMenu.open(player);
    }

    private static OptionClickEvent onClick(OptionClickEvent e){
        //Changeclass
        int pos = e.getPosition();
        final Player p = e.getPlayer();
        if(pos == 0){
            if(p.hasPermission("admin.chat")) {
                if(chatDisabled) {
                    chatDisabled = false;
                    e.setWillClose(true);
                    p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1F, 1F);
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "ALERT " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "Chat has been enabled by " + ChatColor.AQUA + p.getName() + ChatColor.GREEN + "! Everyone is able to chat again.");
                }
                else {
                    chatDisabled = true;
                    e.setWillClose(true);
                    p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1F, 1F);
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "ALERT " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "Chat has been disabled by " + ChatColor.AQUA + p.getName() + ChatColor.RED + "! Currently only staff members are allowed to chat.");
                }
            }
            else {
                e.setWillClose(true);
                p.sendMessage(ChatColor.RED + "You don't have permissions to use this!");
            }
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
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
