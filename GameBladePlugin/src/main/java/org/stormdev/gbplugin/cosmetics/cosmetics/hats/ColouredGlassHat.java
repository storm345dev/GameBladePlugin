package org.stormdev.gbplugin.cosmetics.cosmetics.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
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
		@Override public String getUserFriendlyName()  {	return "White Glass Hat";	}
	}
	public static class OrangeGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "orangeglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 1); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Orange Glass Hat";	}
	}
	public static class MagentaGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "magentaglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 2); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Magenta Glass Hat";	}
	}
	public static class LightBlueGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "lightblueglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 3); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Light Blue Glass Hat";	}
	}
	public static class YellowGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "yellowglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 4); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Yellow Glass Hat";	}
	}
	public static class LimeGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "limeglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 5); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Lime Glass Hat";	}
	}
	public static class PinkGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "pinkglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 6); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Pink Glass Hat";	}
	}
	public static class GrayGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "grayglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 7); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Gray Glass Hat";	}
	}
	public static class LightGrayGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "lightgrayglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 8); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Light Gray Glass Hat";	}
	}
	public static class CyanGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "cyanglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 9); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Cyan Glass Hat";	}
	}
	public static class PurpleGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "purpleglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 10); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Purple Glass Hat";	}
	}
	public static class BlueGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "blueglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 11); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Blue Glass Hat";	}
	}
	public static class BrownGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "brownglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 12); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Brown Glass Hat";	}
	}
	public static class GreenGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "greenglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 13); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Green Glass Hat";	}
	}
	public static class RedGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "redglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 14); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Red Glass Hat";	}
	}
	public static class BlackGlassHat extends BlockhatBase{
		@Override public String getID() {	 return "blackglasshat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.STAINED_GLASS, 1, (byte) 15); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Black Glass Hat";	}
	}
}
