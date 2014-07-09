package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class WaterHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "waterhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.WATER);
	}

	@Override
	public double getPrice() {
		return 1049;
	}

	@Override
	public String getUserFriendlyName() {
		return "Water Hat (Worn on back)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM_PLUS;
	}

}
