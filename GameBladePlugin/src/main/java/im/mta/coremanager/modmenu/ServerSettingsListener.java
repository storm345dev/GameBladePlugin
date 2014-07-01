package im.mta.coremanager.modmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class ServerSettingsListener implements Listener {

    public ServerSettingsListener(){
        Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if(ServerSettingsMenu.chatDisabled){
            if(!p.hasPermission("admin.chat.bypass")){
                p.sendMessage(ChatColor.RED + "Chat is currently only available for staff members.");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if(ServerSettingsMenu.chatDisabled) {
            Bukkit.getScheduler().runTaskLater(GameBlade.plugin, new Runnable() {
                @Override
                public void run() {
                    p.sendMessage(ChatColor.RED
                            + "Chat has been disabled! Only staff members are allowed to chat.");
                }
            }, 20L);
        }
    }

}
