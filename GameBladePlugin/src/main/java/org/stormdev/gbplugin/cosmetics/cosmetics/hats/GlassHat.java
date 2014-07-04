package org.stormdev.gbplugin.cosmetics.cosmetics.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GlassHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "glasshat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.GLASS);
	}

	@Override
	public double getPrice() {
		return 49;
	}

	@Override
	public String getUserFriendlyName() {
		return "Clear Glass Hat";
	}

}
