package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Rank;

public class RainbowHat extends ChangingHat {
	private static String META = "rainbowhatmeta";
	
	@Override
	public ItemStack getHeadWear() {
		return new ItemStack(Material.PAPER);
	}

	@Override
	public String getID() {
		return "rainbowhat";
	}

	@Override
	public double getPrice() {
		return 549;
	}

	@Override
	public String getUserFriendlyName() {
		return "Rainbow Hat (Changes colour)";
	}
	
	@Override
	public Rank minimumRank(){
		return Rank.PREMIUM;
	}
	
	public void switchHat(Player player){
		ItemStack i = player.getInventory().getHelmet();
		if(i == null || !(i.getType().equals(Material.STAINED_GLASS))){
			i = new ItemStack(Material.STAINED_GLASS, 1, (byte) 11);
		}
		
		int b = i.getData().getData();
		b++;
		if(b > 15){
			b = 0;
		}
		
		byte by = (byte) b;
		i = new ItemStack(Material.STAINED_GLASS, 1, (byte) by);
		
		player.getInventory().setHelmet(i);
	}

	@Override
	public String getMeta() {
		return META;
	}

}
