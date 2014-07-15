package org.stormdev.gbplugin.plugin.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stormdev.gbapi.villagers.VillagerManager.VillagerAlreadyRegisteredException;
import org.stormdev.gbapi.villagers.VillagerManager.VillagerListener;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CosmeticVillagers {
	public static void register(){
		try {
			GameBlade.api.getMenuVillagerManager().registerVillagerListener("Cosmetics", new VillagerListener(){

				@Override
				public void onInteract(final Player player) {
					Bukkit.getScheduler().runTask(GameBlade.plugin, new Runnable(){

						@Override
						public void run() {
							CosmeticPanel.getInstance().open(player);
							return;
						}});
					return;
				}});
		} catch (VillagerAlreadyRegisteredException e) {
			// OH NO
			GameBlade.logger.info("ERROR: Could not register cosmetic villager correctly! Plugin conflict!");
		}
	}
}
