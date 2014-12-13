package org.stormdev.gbplugin.plugin.cosmetics.carts;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.inventory.ItemStack;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class CartFiller {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void putBlockInCar(Minecart car, int id, int data){
		ItemStack item = new ItemStack(id, 1, (short) data);
		car.setDisplayBlock(item.getData());
		/*Boolean useFallingBlock = false;
		// net.minecraft.server.v1_7_R1.EntityMinecartAbstract;
		// org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
		String NMSversion = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage()
				.getName().replace(".", ",").split(",")[3];
		String CBversion = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage()
				.getName().replace(".", ",").split(",")[3];
		Class nms = null;
		Class cb = null;
		Class nmsEntity = null;
		try {
			nms = Class.forName(NMSversion + ".EntityMinecartAbstract");
			nmsEntity = Class.forName(NMSversion + ".Entity");
			cb = Class.forName(CBversion + ".entity.CraftEntity");
			Method carId = nms.getMethod("k", int.class);
			Method carData = nms.getMethod("l", int.class); //Method 'm' is for height/offset
			//Method carOffset = nms.getMethod("m", int.class);
			Method getNMSEntity = cb.getMethod("getHandle");
			carId.setAccessible(true);
			carData.setAccessible(true);
			getNMSEntity.setAccessible(true);
			Object ce = cb.cast(car);
			Object nmsE = nmsEntity.cast(getNMSEntity.invoke(ce));
			carId.invoke(nmsE, id);
			carData.invoke(nmsE, data);
		} catch (Exception e) {
			useFallingBlock = true;
		}
		if(useFallingBlock){
			//Don't use falling blocks as they're derpy
			GameBlade.logger.info("[ALERT] Unable to place a block in a car,"
					+ " please check for an update.");
		}*/
		return;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void putBlockInCar(Minecart car, int id, int data, int offset){
		ItemStack item = new ItemStack(id, 1, (short) data);
		car.setDisplayBlock(item.getData());
		car.setDisplayBlockOffset(offset);
		/*Boolean useFallingBlock = false;
		// net.minecraft.server.v1_7_R1.EntityMinecartAbstract;
		// org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
		String NMSversion = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage()
				.getName().replace(".", ",").split(",")[3];
		String CBversion = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage()
				.getName().replace(".", ",").split(",")[3];
		Class nms = null;
		Class cb = null;
		Class nmsEntity = null;
		try {
			nms = Class.forName(NMSversion + ".EntityMinecartAbstract");
			nmsEntity = Class.forName(NMSversion + ".Entity");
			cb = Class.forName(CBversion + ".entity.CraftEntity");
			Method carId = nms.getMethod("k", int.class);
			Method carData = nms.getMethod("l", int.class); //Method 'm' is for height/offset
			Method carOffset = nms.getMethod("m", int.class);
			Method getNMSEntity = cb.getMethod("getHandle");
			carId.setAccessible(true);
			carData.setAccessible(true);
			carOffset.setAccessible(true);
			getNMSEntity.setAccessible(true);
			Object ce = cb.cast(car);
			Object nmsE = nmsEntity.cast(getNMSEntity.invoke(ce));
			
			carId.invoke(nmsE, id);
			carData.invoke(nmsE, data);
			carOffset.invoke(nmsE, offset);
			
		} catch (Exception e) {
			useFallingBlock = true;
		}
		if(useFallingBlock){
			//Don't use falling blocks as they're derpy
			GameBlade.logger.info("[ALERT] Unable to place a block in a car,"
					+ " please check for an update.");
		}*/
		return;
	}
}
