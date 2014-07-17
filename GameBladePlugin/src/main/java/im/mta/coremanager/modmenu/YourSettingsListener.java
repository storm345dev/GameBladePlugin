package im.mta.coremanager.modmenu;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
    
    @EventHandler
    void onQuit(PlayerQuitEvent event){
    	Player p = event.getPlayer();
    	if(ModSettingsMenu.vanishArray.contains(p.getName())){
    		ModSettingsMenu.vanishArray.remove(p.getName());
    	}
    	if(ModSettingsMenu.staffModeArray.contains(p.getName())){
    		ModSettingsMenu.staffModeArray.remove(p.getName());
    		p.setGameMode(GameMode.ADVENTURE);
            p.setHealthScaled(false);
            p.removeMetadata("invincible", GameBlade.plugin);
            p.setAllowFlight(false);
    	}
    }
}
