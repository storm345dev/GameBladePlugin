package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class CopHat extends ChangingHat {
	private static String META = "cophatmeta";

	@Override
	public String getID() {
		return "cophat";
	}

	@Override
	public double getPrice() {
		return 149;
	}

	@Override
	public String getUserFriendlyName() {
		return "Cop Hat (Changes colour)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM_PLUS;
	}
	
	public void switchHat(Player player){
		ItemStack i = player.getInventory().getHelmet();
		if(i == null){
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 11);
		}
		
		if(i.getData().getData() == (byte) 11){
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 14);
		}
		else {//if(i.getData().getData() == (byte) 14)
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 11);
		}
		
		player.getInventory().setHelmet(i);
	}

	@Override
	public String getMeta() {
		return META;
	}

}
