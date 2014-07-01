package im.mta.coremanager.modmenu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class YourSettingsListener implements Listener {

    public YourSettingsListener() {
        Bukkit.getPluginManager().registerEvents(this, GameBlade.plugin);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            if (!staff.getName().equalsIgnoreCase(p.getName())) {
                if (ModSettingsMenu.vanishArray.contains(staff.getName())) {
                    p.hidePlayer(staff);
                }
            }
        }
    }
}
