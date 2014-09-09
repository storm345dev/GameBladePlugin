package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class SkullHats {
	public static void registerAll(){
		Class<SkullHats> c = SkullHats.class;
		Class<?>[] classes = c.getDeclaredClasses();
		for(Class<?> cl:classes){
			if(Cosmetic.class.isAssignableFrom(cl)){
				try {
					CosmeticManager.registerCosmetic((Cosmetic)cl.newInstance());
				} catch (InstantiationException e) {
					//e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static abstract class SkullHatBase extends BlockhatBase {
		@Override
		public void remove(Player player){
			player.getInventory().setHelmet(null);
		}
	}
	
	public static class SteveHat extends SkullHatBase{
		@Override public String getID() {	 return "stevehat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.SKULL_ITEM, 1, (byte) 3); 	}
		@Override public double getPrice() { 	return 49;	}
		@Override public String getUserFriendlyName()  {	return "Steve Hat";	}
	}
	
	public static class SkeletonHat extends SkullHatBase{
		@Override public String getID() {	 return "skelhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.SKULL_ITEM, 1, (byte) 0); 	}
		@Override public double getPrice() { 	return 249;	}
		@Override public String getUserFriendlyName()  {	return "Skeleton Hat";	}
	}
	
	public static class WitherSkeletonHat extends SkullHatBase{
		@Override public String getID() {	 return "wskelhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.SKULL_ITEM, 1, (byte) 1); 	}
		@Override public double getPrice() { 	return 549;	}
		@Override public String getUserFriendlyName()  {	return "Wither Skeleton Hat";	}
	}
	
	public static class ZombieHat extends SkullHatBase{
		@Override public String getID() {	 return "zombhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.SKULL_ITEM, 1, (byte) 2); 	}
		@Override public double getPrice() { 	return 449;	}
		@Override public String getUserFriendlyName()  {	return "Zombie Hat";	}
	}
	
	public static class CreeperHat extends SkullHatBase{
		@Override public String getID() {	 return "creephat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.SKULL_ITEM, 1, (byte) 4); 	}
		@Override public double getPrice() { 	return 1049;	}
		@Override public String getUserFriendlyName()  {	return "Creeper Hat";	}
		@Override public Rank minimumRank() {	return Rank.VIP;	}
	}
}
