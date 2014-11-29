package org.stormdev.gbplugin.plugin.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public enum ParticleEffects {
	
	

	HUGE_EXPLODE(EnumParticle.EXPLOSION_HUGE.name(), 0), LARGE_EXPLODE(EnumParticle.EXPLOSION_LARGE.name(), 1), FIREWORK_SPARK(
			EnumParticle.FIREWORKS_SPARK.name(), 2), AIR_BUBBLE(EnumParticle.WATER_BUBBLE.name(), 3), SUSPEND(EnumParticle.SUSPENDED.name(), 4), DEPTH_SUSPEND(
			EnumParticle.SUSPENDED_DEPTH.name(), 5), TOWN_AURA(EnumParticle.TOWN_AURA.name(), 6), CRITICAL_HIT(EnumParticle.CRIT.name(),
			7), MAGIC_CRITICAL_HIT(EnumParticle.CRIT_MAGIC.name(), 8), MOB_SPELL(EnumParticle.SPELL_MOB.name(), 9), MOB_SPELL_AMBIENT(
					EnumParticle.SPELL_MOB_AMBIENT.name(), 10), SPELL(EnumParticle.SPELL.name(), 11), INSTANT_SPELL(
			EnumParticle.SPELL_INSTANT.name(), 12), BLUE_SPARKLE(EnumParticle.SPELL_WITCH.name(), 13), NOTE_BLOCK(
			EnumParticle.NOTE.name(), 14), ENDER(EnumParticle.PORTAL.name(), 15), ENCHANTMENT_TABLE(
			EnumParticle.ENCHANTMENT_TABLE.name(), 16), EXPLODE(EnumParticle.EXPLOSION_NORMAL.name(), 17), FIRE(EnumParticle.FLAME.name(), 18), LAVA_SPARK(
			EnumParticle.LAVA.name(), 19), FOOTSTEP(EnumParticle.FOOTSTEP.name(), 20), SPLASH(EnumParticle.WATER_SPLASH.name(), 21), LARGE_SMOKE(
			EnumParticle.SMOKE_LARGE.name(), 22), CLOUD(EnumParticle.CLOUD.name(), 23), REDSTONE_DUST(EnumParticle.REDSTONE.name(), 24), SNOWBALL_HIT(
			EnumParticle.SNOWBALL.name(), 25), DRIP_WATER(EnumParticle.DRIP_WATER.name(), 26), DRIP_LAVA(
			EnumParticle.DRIP_LAVA.name(), 27), SNOW_DIG(EnumParticle.SNOW_SHOVEL.name(), 28), SLIME(EnumParticle.SLIME.name(), 29), HEART(
			EnumParticle.HEART.name(), 30), ANGRY_VILLAGER(EnumParticle.VILLAGER_ANGRY.name(), 31), GREEN_SPARKLE(
			EnumParticle.VILLAGER_HAPPY.name(), 32), ICONCRACK(EnumParticle.BLOCK_CRACK.name(), 33), TILECRACK(
			EnumParticle.BLOCK_CRACK.name(), 34);

	private static Object createPacket(ParticleEffects effect,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) throws Exception {
		if (count <= 0) {
			count = 1;
		}
		
		Constructor<?>[] cc = PacketPlayOutWorldParticles.class.getDeclaredConstructors();
		for(Constructor<?> c:cc){
			Class<?>[] classes = c.getParameterTypes();
			StringBuilder sb = new StringBuilder();
			for(Class<?> cl:classes){
				sb.append("[").append(cl.getName()).append("]");
			}
			System.out.println(sb.toString());
		}
		
		//[Enum Particle (NMS)] [Boolean] [Float] [Float] [Float] [Float] [Float] [Float] [Float] [Float] [int] [int...]
		PacketPlayOutWorldParticles pp = new PacketPlayOutWorldParticles(EnumParticle.valueOf(effect.name.toUpperCase()), true, (float) location.getX(), (float) location.getY(),
				(float) location.getZ(), offsetX, offsetY, offsetZ, speed,
				count);
		return pp;
	}
	
	private static Object createPacket(EnumParticle effect,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) throws Exception {
		if (count <= 0) {
			count = 1;
		}
		
		Constructor<?>[] cc = PacketPlayOutWorldParticles.class.getDeclaredConstructors();
		for(Constructor<?> c:cc){
			Class<?>[] classes = c.getParameterTypes();
			StringBuilder sb = new StringBuilder();
			for(Class<?> cl:classes){
				sb.append("[").append(cl.getName()).append("]");
			}
			System.out.println(sb.toString());
		}
		
		//[Enum Particle (NMS)] [Boolean] [Float] [Float] [Float] [Float] [Float] [Float] [Float] [Float] [int] [int...]
		PacketPlayOutWorldParticles pp = new PacketPlayOutWorldParticles(effect, true, (float) location.getX(), (float) location.getY(),
				(float) location.getZ(), offsetX, offsetY, offsetZ, speed,
				count);
		return pp;
	}

	/*private static Class<?> getCraftClass(String name) {
		String version = getVersion() + ".";
		String className = "net.minecraft.server." + version + name;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}*/

	private static Object getHandle(Entity entity) {
		try {
			Method entity_getHandle = entity.getClass().getMethod("getHandle");
			Object nms_entity = entity_getHandle.invoke(entity);
			return nms_entity;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*private static String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName()
				.replace(".", ",").split(",")[3];
	}*/

	private static void sendPacket(Player p, Object packet) throws Exception {
		Object eplayer = getHandle(p);
		Field playerConnectionField = eplayer.getClass().getField(
				"playerConnection");
		Object playerConnection = playerConnectionField.get(eplayer);
		for (Method m : playerConnection.getClass().getMethods()) {
			if (m.getName().equalsIgnoreCase("sendPacket")) {
				m.invoke(playerConnection, packet);
				return;
			}
		}
	}

	/**
	 * Send a particle effect to all players
	 * 
	 * @param effect
	 *            The particle effect to send
	 * @param location
	 *            The location to send the effect to
	 * @param offsetX
	 *            The x range of the particle effect
	 * @param offsetY
	 *            The y range of the particle effect
	 * @param offsetZ
	 *            The z range of the particle effect
	 * @param speed
	 *            The speed (or color depending on the effect) of the particle
	 *            effect
	 * @param count
	 *            The count of effects
	 */
	public static void sendToLocation(ParticleEffects effect,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) {
		try {
			Object packet = createPacket(effect, location, offsetX, offsetY,
					offsetZ, speed, count);
			for (Player player : Bukkit.getOnlinePlayers()) {
				sendPacket(player, packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendToLocation(EnumParticle effect,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) {
		try {
			Object packet = createPacket(effect, location, offsetX, offsetY,
					offsetZ, speed, count);
			for (Player player : Bukkit.getOnlinePlayers()) {
				sendPacket(player, packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a particle effect to a player
	 * 
	 * @param effect
	 *            The particle effect to send
	 * @param player
	 *            The player to send the effect to
	 * @param location
	 *            The location to send the effect to
	 * @param offsetX
	 *            The x range of the particle effect
	 * @param offsetY
	 *            The y range of the particle effect
	 * @param offsetZ
	 *            The z range of the particle effect
	 * @param speed
	 *            The speed (or color depending on the effect) of the particle
	 *            effect
	 * @param count
	 *            The count of effects
	 */
	public static void sendToPlayer(ParticleEffects effect, Player player,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) {
		try {
			Object packet = createPacket(effect, location, offsetX, offsetY,
					offsetZ, speed, count);
			sendPacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String name;

	private int id;

	ParticleEffects(String name, int id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * Gets the id of the Particle Effect
	 * 
	 * @return The id of the Particle Effect
	 */
	int getId() {
		return id;
	}

	/**
	 * Gets the name of the Particle Effect
	 * 
	 * @return The particle effect name
	 */
	String getName() {
		return name;
	}

}