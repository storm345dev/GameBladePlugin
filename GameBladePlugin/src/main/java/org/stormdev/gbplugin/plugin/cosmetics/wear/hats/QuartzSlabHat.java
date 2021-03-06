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
		return new ItemStack(Material.STEP, 1, (byte) 7);
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Quartz Slab Hat (Like Mask)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.VIP;
	}

}
