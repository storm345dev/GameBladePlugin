package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GlowstoneHat extends BlockhatBase {
	
	@Override
	public String getID() {
		return "glowhat";
	}

	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.GLOWSTONE);
	}

	@Override
	public double getPrice() {
		return 149;
	}

	@Override
	public String getUserFriendlyName() {
		return "Glowstone Hat";
	}

}
