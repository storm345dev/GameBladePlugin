package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public abstract class PlayerSkullHat extends BlockhatBase {
	
	public static void registerAll(){
		Class<PlayerSkullHat> c = PlayerSkullHat.class;
		Class<?>[] classes = c.getDeclaredClasses();
		for(Class<?> cl:classes){
			if(Cosmetic.class.isAssignableFrom(cl)){
				try {
					CosmeticManager.registerCosmetic((Cosmetic)cl.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public abstract String getPlayerName();
	
	@Override
	public ItemStack getHeadWear() {
		ItemStack profile = new ItemStack(Material.SKULL_ITEM, 1);
		SkullMeta skullMeta = (SkullMeta) profile.getItemMeta();
		profile.setDurability((short) 3);
		skullMeta.setDisplayName(getPlayerName());
		skullMeta.setOwner(getPlayerName());
		profile.setItemMeta(skullMeta);
		return profile;
	}

	@Override
	public double getPrice() {
		return 1049;
	}
	
	@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	
	public static class StormHat extends PlayerSkullHat{
		@Override public String getID() {	 return "stormhat";	 }
		@Override public String getUserFriendlyName()  {	return "Storm Hat";	  }
		@Override public String getPlayerName() { 	return "storm345";  	}
	}
	
	public static class BjarnHat extends PlayerSkullHat{
		@Override public String getID() {	 return "bjarnhat";	 }
		@Override public String getUserFriendlyName()  {	return "Bjarn Hat";	  }
		@Override public String getPlayerName() { 	return "ItsJustBjarn";  	}
	}
	
	public static class JesseHat extends PlayerSkullHat{
		@Override public String getID() {	 return "jessehat";	 }
		@Override public String getUserFriendlyName()  {	return "Jesse Hat";	  }
		@Override public String getPlayerName() { 	return "FlamingxGamer‏";  	}
	}
	
	public static class ShaneHat extends PlayerSkullHat{
		@Override public String getID() {	 return "shyanehat";	 }
		@Override public String getUserFriendlyName()  {	return "McPhillygin Hat";	  }
		@Override public String getPlayerName() { 	return "McPhillygin";  	}
	}
	
	public static class DurpyHat extends PlayerSkullHat{
		@Override public String getID() {	 return "durpyhat";	 }
		@Override public String getUserFriendlyName()  {	return "xDurpyx Hat";	  }
		@Override public String getPlayerName() { 	return "xDurpyx";  	}
	}
	
	public static class StijnHat extends PlayerSkullHat{
		@Override public String getID() {	 return "stijnhat";	 }
		@Override public String getUserFriendlyName()  {	return "Stijn Hat";	  }
		@Override public String getPlayerName() { 	return "just_stijn";  	}
	}
	
	public static class NotchHat extends PlayerSkullHat{
		@Override public String getID() {	 return "notchhat";	 }
		@Override public String getUserFriendlyName()  {	return "Notch Hat";	  }
		@Override public String getPlayerName() { 	return "Notch";  	}
	}
	
	public static class DinnerboneHat extends PlayerSkullHat{
		@Override public String getID() {	 return "dinbonehat";	 }
		@Override public String getUserFriendlyName()  {	return "Dinnerbone Hat";	  }
		@Override public String getPlayerName() { 	return "Dinnerbone";  	}
	}

}
