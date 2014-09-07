package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class CarpetShiftHat extends ChangingHat {
	private static String META = "carpetshifthatmeta";
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PAPER);
	}

	@Override
	public String getID() {
		return "carpetshifthat";
	}

	@Override
	public double getPrice() {
		return 549;
	}

	@Override
	public String getUserFriendlyName() {
		return "Carpet Shift Hat (Changes carpet colour)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.ULTIMATE;
	}
	
	public void switchHat(Player player){
		ItemStack i = player.getInventory().getHelmet();
		if(i == null || !(i.getType().equals(Material.CARPET))){
			i = new ItemStack(Material.CARPET, 1, (byte) 11);
		}
		
		int b = i.getData().getData();
		b++;
		if(b > 7){
			b = 0;
		}
		
		byte by = (byte) b;
		i = new ItemStack(Material.CARPET, 1, (byte) by);
		
		player.getInventory().setHelmet(i);
	}

	@Override
	public String getMeta() {
		return META;
	}

}
