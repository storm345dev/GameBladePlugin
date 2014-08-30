package org.stormdev.gbplugin.api.stars;

public class StarMultipliers {
	public static final double VIP_MULTIPLIER = 1.25;
	public static final double VIP_PLUS_MULTIPLIER = 1.5;
	public static final double PREMIUM_MULTIPLIER = 2.0;
	public static final double PREMIUM_PLUS_MULTIPLIER = 2.25;
	public static final double ULTIMATE_MULTIPLIER = 3.0;
	
	public static double getMultiplier(String rank){
		if(rank.equalsIgnoreCase("vip")){
			return VIP_MULTIPLIER;
		}
		else if(rank.equalsIgnoreCase("vip+")
				||rank.equalsIgnoreCase("vipplus")
				||rank.equalsIgnoreCase("vip_plus")
				||rank.equalsIgnoreCase("vip-plus")){
			return VIP_PLUS_MULTIPLIER;
		}
		else if(rank.equalsIgnoreCase("premium")){
			return PREMIUM_MULTIPLIER;
		}
		else if(rank.equalsIgnoreCase("premium+")
				||rank.equalsIgnoreCase("premiumplus")
				||rank.equalsIgnoreCase("premium_plus")
				||rank.equalsIgnoreCase("premium-plus")){
			return PREMIUM_PLUS_MULTIPLIER;
		}
		else if(rank.equalsIgnoreCase("ultimate")){
			return ULTIMATE_MULTIPLIER;
		}
		return 1.0;
	}
}
