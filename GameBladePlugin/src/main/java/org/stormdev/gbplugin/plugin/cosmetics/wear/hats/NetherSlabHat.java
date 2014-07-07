package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class NetherSlabHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "netherslabhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.STEP, 1, (byte) 6);
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Nether Brick Slab Hat (Like ninja Mask)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.VIP;
	}

}
