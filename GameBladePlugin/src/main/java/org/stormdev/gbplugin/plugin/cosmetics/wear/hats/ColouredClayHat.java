package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class ColouredClayHat {
	public static void registerAll(){
		Class<ColouredClayHat> c = ColouredClayHat.class;
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
	
	public static class WhiteClayHat extends BlockhatBase{
		@Override public String getID() {	 return "whiteclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 0); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (White)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class OrangeClayHat extends BlockhatBase{
		@Override public String getID() {	 return "orangeclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 1); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Orange)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class MagentaClayHat extends BlockhatBase{
		@Override public String getID() {	 return "magentaclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 2); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Magenta)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class LightBlueClayHat extends BlockhatBase{
		@Override public String getID() {	 return "lightblueclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 3); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Light Blue)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class YellowClayHat extends BlockhatBase{
		@Override public String getID() {	 return "yellowclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 4); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Yellow)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class LimeClayHat extends BlockhatBase{
		@Override public String getID() {	 return "limeclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 5); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Lime)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class PinkClayHat extends BlockhatBase{
		@Override public String getID() {	 return "pinkclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 6); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Pink)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class GrayClayHat extends BlockhatBase{
		@Override public String getID() {	 return "grayclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 7); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Gray)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class LightGrayClayHat extends BlockhatBase{
		@Override public String getID() {	 return "lightgrayclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 8); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Light Gray)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class CyanClayHat extends BlockhatBase{
		@Override public String getID() {	 return "cyanclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 9); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Cyan)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class PurpleClayHat extends BlockhatBase{
		@Override public String getID() {	 return "purpleclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 10); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Purple)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class BlueClayHat extends BlockhatBase{
		@Override public String getID() {	 return "blueclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 11); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Blue)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class BrownClayHat extends BlockhatBase{
		@Override public String getID() {	 return "brownclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 12); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Brown)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class GreenClayHat extends BlockhatBase{
		@Override public String getID() {	 return "greenclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 13); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Green)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class RedClayHat extends BlockhatBase{
		@Override public String getID() {	 return "redclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 14); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Red)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
	public static class BlackClayHat extends BlockhatBase{
		@Override public String getID() {	 return "blackclayhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_CLAY, 1, (byte) 15); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Clay Hat (Black)";	}
		@Override public Rank minimumRank() {	return Rank.PREMIUM_PLUS; 	}
	}
}
