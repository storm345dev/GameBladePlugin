package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class RailHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "railhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.POWERED_RAIL);
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Rail Hat (Worn on back)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM;
	}

}
