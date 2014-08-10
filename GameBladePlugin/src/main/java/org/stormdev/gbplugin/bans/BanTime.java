package org.stormdev.gbplugin.bans;

import org.stormdev.gbapi.bans.BanHandler.Time;

public enum BanTime {
	FOREVER(true, 1, "Forever"), HOUR(false, Times.hour, "An Hour"), DAY(false, Times.day, "A Day"), WEEK(false, Times.week, "A Week"), MONTH(false, Times.month, "A Month"), 
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
	
	public static Time getFromUserInput(String input){
		input = input.toLowerCase();
		if(input.equalsIgnoreCase("") || input.equalsIgnoreCase("forever")){
			return FOREVER.getNewTime();
		}
		// "+\d{1}\D" -> regex for 9a, 5g, etc
		if(!input.matches("\\d+\\D{1}")){
			if(!input.matches("\\d+")){
				return null;
			}
			System.out.println(Long.parseLong(input)+"minutes");
			return new Time((long)(Times.minute*Long.parseLong(input)));
		}
		String timeRaw = input.replaceAll("\\D", "");//Remove all non-numbers
		String timeMode = input.replaceAll("\\d", ""); //Remove all numbers
		
		long time = Long.parseLong(timeRaw);
		long duration = 1*Times.minute;
		
		if(timeMode.equalsIgnoreCase("s")){
			duration = time*(1/60)*Times.minute;
		}
		else if(timeMode.equalsIgnoreCase("h")){
			duration = time * Times.hour;
		}
		else if(timeMode.equalsIgnoreCase("d")){
			duration = time * Times.day;
		}
		else if(timeMode.equalsIgnoreCase("w")){
			duration = time * Times.week;
		}
		else if(timeMode.equalsIgnoreCase("m")){
			duration = time * Times.month;
		}
		else if(timeMode.equalsIgnoreCase("y")){
			duration = time * Times.month*12;
		}
		return new Time(duration);
	}
}
