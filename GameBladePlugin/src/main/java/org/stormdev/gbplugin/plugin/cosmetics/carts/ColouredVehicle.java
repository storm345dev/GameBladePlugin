package org.stormdev.gbplugin.plugin.cosmetics.carts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.stormdev.gbapi.storm.misc.MetadataValue;
import org.stormdev.gbplugin.plugin.core.GameBlade;
import org.stormdev.stormapi.misc.ObjectWrapper;

public interface ColouredVehicle {
	public void apply(Minecart m, Player player);
	public void remove(Minecart m);
	
	public static class BlockVehicleColour implements ColouredVehicle {
		private static final int defaultOffset = 15;
		
		private int id;
		private int data;
		private int offset = defaultOffset;
		
		public BlockVehicleColour(ItemStack stack, int offset){
			this(stack.getTypeId(), stack.getData().getData(), offset);
		}
		
		public BlockVehicleColour(ItemStack stack){
			this(stack.getTypeId(), stack.getData().getData());
		}
		
		public BlockVehicleColour(int id){
			this(id, 0);
		}
		
		public BlockVehicleColour(int id, int data){
			this(id, data, defaultOffset);
		}
		
		public BlockVehicleColour(int id, int data, int offset){
			this.id = id;
			this.data = data;
			this.offset = offset;
		}

		@Override
		public void apply(Minecart m, Player player) {
			CartFiller.putBlockInCar(m, id, data, offset);
		}

		@Override
		public void remove(Minecart m) {
			CartFiller.putBlockInCar(m, 0, 0);
		}
		
	}
	
	public static class BlockChangingVehicleColour implements ColouredVehicle {
		private BlockVehicleColour[] colours;
		private long interval;
		private static final String TASK_META = "vehicle.change";
		private static final String TASK_META_VAL = "vehicle.changepos";
		
		public BlockChangingVehicleColour(long interval, BlockVehicleColour... colours){
			this.colours = colours;
			this.interval = interval;
			if(colours.length < 1){
				throw new RuntimeException("List of states MUST not be null!");
			}
		}
		
		public void cancel(Minecart cart){
			cart.removeMetadata(TASK_META_VAL, GameBlade.plugin);
			if(!cart.hasMetadata(TASK_META)){
				return;
			}
			
			Object o = cart.getMetadata(TASK_META).get(0).value();
			if(o instanceof BukkitTask){
				BukkitTask task = (BukkitTask) o;
				task.cancel();
			}
			cart.removeMetadata(TASK_META, GameBlade.plugin);
			cart.removeMetadata(TASK_META_VAL, GameBlade.plugin);
		}
		
		public void changeColour(Minecart m){
			int pos = 0;
			if(m.hasMetadata(TASK_META_VAL)){
				Object o;
				try {
					o = m.getMetadata(TASK_META_VAL).get(0).value();
				} catch (Exception e1) {
					o = null;
				}
				if(o != null){
					String s = o.toString();
					try {
						pos = Integer.parseInt(s);
					} catch (NumberFormatException e) {
						//Oh well
					}
				}
			}
			
			if(pos >= colours.length){
				pos = 0;
			}
			BlockVehicleColour c = colours[pos];
			c.apply(m, null);
			
			pos++;
			if(pos >= colours.length){
				pos = 0;
			}
			
			m.setMetadata(TASK_META_VAL, new MetadataValue(pos, GameBlade.plugin));
		}

		@Override
		public void apply(final Minecart m, final Player player) {
			cancel(m); //If it's already going, stop it
			
			final ObjectWrapper<BukkitTask> wrap = new ObjectWrapper<BukkitTask>();
			wrap.setValue(Bukkit.getScheduler().runTaskTimer(GameBlade.plugin, new Runnable(){

				@Override
				public void run() {
					if(player == null || !player.isOnline()){
						wrap.getValue().cancel();
						if(m != null){
							m.removeMetadata(TASK_META, GameBlade.plugin);
							m.removeMetadata(TASK_META_VAL, GameBlade.plugin);
						}
						return;
					}
					if(m == null || !m.isValid() || m.isDead() || m.isEmpty()){
						wrap.getValue().cancel();
						if(m != null){
							m.removeMetadata(TASK_META, GameBlade.plugin);
							m.removeMetadata(TASK_META_VAL, GameBlade.plugin);
						}
						return;
					}
					
					if(player.getVehicle() == null || !player.getVehicle().equals(m)){
						wrap.getValue().cancel();
						if(m != null){
							m.removeMetadata(TASK_META, GameBlade.plugin);
							m.removeMetadata(TASK_META_VAL, GameBlade.plugin);
						}
						return;
					}
					
					changeColour(m);
					
					return;
				}}, 0l, interval));
		}

		@Override
		public void remove(Minecart m) {
			cancel(m);
			CartFiller.putBlockInCar(m, 0, 0);
		}
	}
}
