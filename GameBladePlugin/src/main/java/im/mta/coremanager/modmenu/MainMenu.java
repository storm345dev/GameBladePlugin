package im.mta.coremanager.modmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.gui.IconMenu;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEvent;
import org.stormdev.gbapi.gui.IconMenu.OptionClickEventHandler;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class MainMenu {

    private static IconMenu mainMenu;

    static {
        mainMenu = new IconMenu(ChatColor.RED + "Main", 9, new OptionClickEventHandler(){

            @Override
            public void onOptionClick(OptionClickEvent event) {
                event.setWillClose(false);
                event.setWillDestroy(false);
                onClick(event);
                return;
            }}, GameBlade.plugin);

        mainMenu.setOption(0,
                new ItemStack(Material.PAPER),
                ChatColor.RED + "Server Stats",
                ChatColor.GRAY + "Online Players: " + ChatColor.WHITE + GameBlade.serverInfo.getPlayerCount(),
                ChatColor.GRAY + "Total RAM: " + ChatColor.WHITE + GameBlade.serverInfo.getMaxRam()+"MB",
                ChatColor.GRAY + "RAM in Use: " + ChatColor.WHITE + GameBlade.serverInfo.getUsedRam()+"MB",
                ChatColor.GRAY + "Free RAM: " + ChatColor.WHITE + (GameBlade.serverInfo.getMaxRam()-GameBlade.serverInfo.getUsedRam())+"MB",
                ChatColor.GRAY + "Resource Score %: " + ChatColor.WHITE + (GameBlade.serverInfo.getResourceScore()),
                ChatColor.GRAY + "TPS: " + ChatColor.WHITE + GameBlade.serverInfo.getTPS());
        mainMenu.setOption(1,
                new ItemStack(Material.BOOK),
                ChatColor.RED + "Server Settings");
        mainMenu.setOption(2,
                new ItemStack(Material.BOOK),
                ChatColor.RED + "Server Actions");
        mainMenu.setOption(4,
                new ItemStack(Material.BOOK),
                ChatColor.RED + "Your Settings");
        mainMenu.setOption(8,
                new ItemStack(Material.IRON_DOOR),
                ChatColor.RED + "Exit");
    }

    public static void open(Player player){
        mainMenu.open(player);
    }

    private static OptionClickEvent onClick(OptionClickEvent e){
        //Changeclass
        int pos = e.getPosition();
        final Player p = e.getPlayer();
        if(pos == 0){
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        }
        else if(pos == 1){
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            e.setWillClose(true);
            Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

                @Override
                public void run() {
                    ServerSettingsMenu.open(p);
                    return;
                }}, 2);
        }
        else if(pos == 2){
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            e.setWillClose(true);
            Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

                @Override
                public void run() {
                    ServerActionsMenu.serverActionsMenu.open(p);
                    return;
                }}, 2);
        }
        else if(pos == 4){
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            e.setWillClose(true);
            Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable(){

                @Override
                public void run() {
                    new ModSettingsMenu(p).open(p);
                    return;
                }}, 2);
        }
        else if(pos == 8){
            e.setWillClose(true);
            p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
        }
        else{
            return e;
        }

        return e;
    }
}
