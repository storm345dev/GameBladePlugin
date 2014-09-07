package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbapi.cosmetics.Rank;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class ColouredGlassHat {
	public static void registerAll(){
		Class<ColouredGlassHat> c = ColouredGlassHat.class;
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
	
	public static class WhiteGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "whiteglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 0); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (White)";	}
	}
	public static class OrangeGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "orangeglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 1); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Orange)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class MagentaGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "magentaglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 2); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Magenta)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class LightBlueGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "lightblueglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 3); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Light Blue)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class YellowGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "yellowglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 4); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Yellow)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class LimeGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "limeglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 5); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Lime)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class PinkGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "pinkglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 6); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Pink)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class GrayGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "grayglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 7); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Gray)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class LightGrayGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "lightgrayglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 8); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Light Gray)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class CyanGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "cyanglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 9); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Cyan)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class PurpleGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "purpleglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 10); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Purple)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class BlueGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "blueglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 11); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Blue)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class BrownGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "brownglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 12); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Brown)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class GreenGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "greenglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 13); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Green)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class RedGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "redglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 14); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Red)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
	public static class BlackGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "blackglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 15); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Glass Hat (Black)";	}
		@Override public Rank minimumRank() {	return Rank.VIP_PLUS;	}
	}
}
