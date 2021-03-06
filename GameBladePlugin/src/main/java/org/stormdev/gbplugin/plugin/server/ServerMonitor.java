package org.stormdev.gbplugin.plugin.server;

import org.bukkit.Bukkit;
import org.stormdev.gbplugin.plugin.core.GameBlade;


public class ServerMonitor implements Runnable {
	public static int TICK_COUNT = 0;
	public static long[] TICKS = new long[600];
	public static long LAST_TICK = 0L;
	private long finalTime = 0L;
	private static boolean running = false;
	private static long lastFailMS = 0;
	private static int failedSyncTasks = 0;
	
	public static void failedSyncTask(){
		lastFailMS = System.currentTimeMillis();
		failedSyncTasks++;
	}

	public static double getTPS() {
		return getTPS(100);
	}

	public static double getAvailableMemory(){
		return Runtime.getRuntime().freeMemory() * 0.00097560975 * 0.00097560975; //In MB
	}
	
	public static double getMaxMemory(){
		return Runtime.getRuntime().maxMemory() * 0.00097560975 * 0.00097560975; //In MB
	}
	
	public static double getMemoryUse(){
		return getMaxMemory()-getAvailableMemory();
	}
	
	public static boolean overloadPrevention(){
		long freeMemory = (long) (Runtime.getRuntime().freeMemory() * 0.00097560975 * 0.00097560975); //In MB
		
		if(freeMemory < 150){
			System.gc();
			freeMemory = (long) (Runtime.getRuntime().freeMemory() * 0.00097560975 * 0.00097560975); //In MB
		}
		return false;
	}
	
	public static int getResourceScore(){
		if(GameBlade.random.nextInt(100) < 10){
			overloadPrevention();
		}
		double tps = getTPS(100);
		double mem = getAvailableMemory();
		if(tps>19.5 && mem>400){
			return 100;
		}
		else if(mem < 50){
			return 10;
		}
		int i = 100;
		i -= 100-(tps*5);
		if(mem < 300){
			i -=20;
		}
		if(i > 84 && tps < 19){
			i = 83;
		}
		return i;
	}
	
	public static int getResourceScore(double requestedMemory){
		if(GameBlade.random.nextInt(100) < 10){
			overloadPrevention();
		}
		if(failedSyncTasks > 5){
			if((System.currentTimeMillis() - lastFailMS) > 20000){
				failedSyncTasks = 0;
			}
			return 10;
		}
		double tps = getTPS(100);
		double mem = getAvailableMemory();
		if(tps>19 && mem>requestedMemory+20){
			return 100;
		}
		else if(mem < requestedMemory){
			return 10;
		}
		else{
			int i = 100;
			i -= 100-(tps*5);
			if(mem < requestedMemory){
				i -=50;
			}
			return i;
		}
	}
	
	public static double getTPS(int ticks) {
		try {
			if (TICK_COUNT < ticks || !running) {
				return 20.0D;
			}
			int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
			long elapsed = System.currentTimeMillis() - TICKS[target];
			return ticks / (elapsed / 1000.0D);
		} catch (Exception e) {
			//Has been restarted or is still loading
			return 20;
		}
	}

	public static long getElapsed(int tickID) {
		long time = TICKS[(tickID % TICKS.length)];
		return System.currentTimeMillis() - time;
	}

	@Override
	public void run() {
		running = true;
		if(finalTime < 1){
			finalTime = System.currentTimeMillis() + 1200000; //20 mins later...
		}
		long current = System.currentTimeMillis();
		if(current > finalTime){
			//Restart
			restart();
			return;
		}
		TICKS[(TICK_COUNT % TICKS.length)] = current;
		TICK_COUNT += 1;
		return;
	}
	
	public void checkMemory(){
		double memPercentOrig = (getMemoryUse()/getMaxMemory()*100d);
		if(memPercentOrig > 85){
			System.gc(); //Garbage collect!
			double memPercent = (getMemoryUse()/getMaxMemory()*100d);
			double improvePercent = (memPercent-memPercentOrig)/memPercentOrig*100;
			GameBlade.logger.info("Attempted to clear used memory, memory freed: "+improvePercent+"%");
		}
		else {
			//GameBlade.logger.info("SYSTEM MEMORY STABLE (using "+memPercentOrig+"%)");
		}
	}
	
	public void start(){
		TICK_COUNT = 0;
		TICKS = new long[600];
		LAST_TICK = 0L;
		running = false;
		//GameBlade.plugin.lagReducer.cancel();
		GameBlade.plugin.serverMonitor = Bukkit.getScheduler().runTaskTimer(GameBlade.plugin,
				new ServerMonitor(), 100L, 1L);
		checkMemory();
		return;
	}
	
	public void restart(){
		TICK_COUNT = 0;
		TICKS = new long[600];
		LAST_TICK = 0L;
		running = false;
		failedSyncTasks = 0;
		GameBlade.plugin.serverMonitor.cancel();
		GameBlade.plugin.serverMonitor = Bukkit.getScheduler().runTaskTimer(GameBlade.plugin,
				new ServerMonitor(), 100L, 1L);
		
		checkMemory();
		return;
	}
}
