package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class RedstoneTorchHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "redtorchhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.REDSTONE_TORCH_ON);
	}

	@Override
	public double getPrice() {
		return 249;
	}

	@Override
	public String getUserFriendlyName() {
		return "Redstone Torch Hat (Worn on back)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.VIP_PLUS;
	}

}
