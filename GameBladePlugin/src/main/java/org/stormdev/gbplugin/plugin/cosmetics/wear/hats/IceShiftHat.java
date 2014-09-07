package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class IceShiftHat extends ChangingHat {
	private static String META = "iceshifthatmeta";
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PAPER);
	}

	@Override
	public String getID() {
		return "iceshifthat";
	}

	@Override
	public double getPrice() {
		return 549;
	}

	@Override
	public String getUserFriendlyName() {
		return "Ice Shift Hat (Changes ice)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM;
	}
	
	public void switchHat(Player player){
		ItemStack i = player.getInventory().getHelmet();
		if(i == null || !(i.getType().equals(Material.ICE))){
			i = new ItemStack(Material.PACKED_ICE);
		}
		
		if(i.getType().equals(Material.ICE)){
			i = new ItemStack(Material.PACKED_ICE);
		}
		else {
			i = new ItemStack(Material.ICE);
		}
		
		player.getInventory().setHelmet(i);
	}

	@Override
	public String getMeta() {
		return META;
	}

}
