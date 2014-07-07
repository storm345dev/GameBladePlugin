package org.stormdev.gbplugin.plugin.cosmetics.wear.hats;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbapi.cosmetics.Cosmetic;
import org.stormdev.gbplugin.plugin.cosmetics.CosmeticManager;

public class ColouredWoolHat {
	public static void registerAll(){
		Class<ColouredWoolHat> c = ColouredWoolHat.class;
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
	
	public static class WhiteWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "whitewoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 0); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (White)";	}
	}
	public static class OrangeWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "orangewoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 1); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Orange)";	}
	}
	public static class MagentaWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "magentawoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 2); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Magenta)";	}
	}
	public static class LightBlueWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "lightbluewoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 3); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Light Blue)";	}
	}
	public static class YellowWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "yellowwoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 4); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Yellow)";	}
	}
	public static class LimeWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "limewoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 5); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Lime)";	}
	}
	public static class PinkWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "pinkwoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 6); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Pink)";	}
	}
	public static class GrayWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "graywoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 7); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Gray)";	}
	}
	public static class LightGrayWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "lightgraywoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 8); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Light Gray)";	}
	}
	public static class CyanWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "cyanwoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 9); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Cyan)";	}
	}
	public static class PurpleWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "purplewoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 10); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Purple)";	}
	}
	public static class BlueWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "bluewoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 11); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Blue)";	}
	}
	public static class BrownWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "brownwoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 12); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Brown)";	}
	}
	public static class GreenWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "greenwoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 13); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Green)";	}
	}
	public static class RedWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "redwoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 14); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Red)";	}
	}
	public static class BlackWoolHat extends BlockhatBase{
		@Override public String getID() {	 return "blackwoolhat";	 }
		@Override public ItemStack getHeadWear() { 	return new ItemStack(Material.WOOL, 1, (byte) 15); 	}
		@Override public double getPrice() { 	return 99;	}
		@Override public String getUserFriendlyName()  {	return "Wool Hat (Black)";	}
	}
}
