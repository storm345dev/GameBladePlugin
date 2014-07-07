package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbapi.storm.misc.MetadataValue;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class BeaconHat extends BlockhatBase {
	private static String META = "beaconhatmeta";
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PAPER);
	}

	@Override
	public String getID() {
		return "beaconhat";
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Beacon Hat (Changes colour)";
	}
	
	@Override
	public boolean apply(final Player player) {
		boolean b = super.apply(player);
		if(!b){
			return false;
		}
		
		BukkitTask t = Bukkit.getScheduler().runTaskTimer(GameBlade.plugin, new Runnable(){

			@Override
			public void run() {
				switchHat(player);
				return;
			}}, 20l, 20l);
		player.removeMetadata(META, GameBlade.plugin);
		player.setMetadata(META, new MetadataValue(t, GameBlade.plugin));
		return true;
	}
	
	private void switchHat(Player player){
		ItemStack i = player.getInventory().getHelmet();
		if(i == null){
			i = new ItemStack(Material.BEACON, 1);
		}
		
		if(i.getType().equals(Material.BEACON)){
			i = new ItemStack(Material.GLOWSTONE, 1);
		}
		else {//if(i.getData().getData() == (byte) 14)
			i = new ItemStack(Material.BEACON, 1);
		}
		
		player.getInventory().setHelmet(i);
	}
	
	@Override
	public void remove(Player player) {
		//Cancel task
		if(player.hasMetadata(META)){
			Object o = player.getMetadata(META).get(0).value();
			if(o instanceof BukkitTask){
				BukkitTask task = (BukkitTask) o;
				task.cancel();
			}
			player.removeMetadata(META, GameBlade.plugin);
		}
		player.getInventory().setHelmet(null);
	}

}
