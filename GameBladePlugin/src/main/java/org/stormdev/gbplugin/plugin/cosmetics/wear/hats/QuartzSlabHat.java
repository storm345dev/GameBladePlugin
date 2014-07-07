package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class QuartzSlabHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "quartzslabhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.QUARTZ_STAIRS, 1, (byte) 1);
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Quartz Stair Hat (Like Mask)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.VIP;
	}

}
