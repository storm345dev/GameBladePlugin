package org.stormdev.gbplugin.bans;

import org.stormdev.gbapi.bans.BanHandler.Time;

public enum BanTime {
	FOREVER(true, 1, "Forever"), HOUR(false, Times.hour, "An Hour"), DAY(false, Times.day, "A Day"), MONTH(false, Times.month, "A Month"), 
	THREE_MONTHS(false, 3*Times.month, "3 Months"), SIX_MONTHS(false, 6*Times.month, "6 Months"), YEAR(false, 12*Times.month, "A Year");
	
	private boolean forever;
	private long duration;
	private String name;
	private BanTime(boolean forever, long duration, String name){
		this.forever = forever;
		this.duration = duration;
		this.name = name;
	}
	
	public Time getNewTime(){
		if(forever){
			return new Time();
		}
		return new Time(duration);
	}
	
	public String getUserFriendlyName(){
		return name;
	}
}
