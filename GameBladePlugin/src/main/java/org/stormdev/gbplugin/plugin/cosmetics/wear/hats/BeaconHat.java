package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BeaconHat extends ChangingHat {
	private static String META = "beaconhatmeta";

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
	
	public void switchHat(Player player){
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
	public String getMeta() {
		return META;
	}

}
