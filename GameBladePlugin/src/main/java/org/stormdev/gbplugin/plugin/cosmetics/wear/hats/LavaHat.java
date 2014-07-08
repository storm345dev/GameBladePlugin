package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class LavaHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "lavahat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.LAVA);
	}

	@Override
	public double getPrice() {
		return 1049;
	}

	@Override
	public String getUserFriendlyName() {
		return "Lava Hat (Worn on back)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM_PLUS;
	}

}
