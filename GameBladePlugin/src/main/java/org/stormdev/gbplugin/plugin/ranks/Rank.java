package org.stormdev.gbplugin.plugin.ranks;

public enum Rank {
DEFAULT(1),
VIP(2),
VIP_PLUS(3),
PREMIUM(4),
PREMIUM_PLUS(5),
STAFF(6),
EPIC_STAFF(7),
ULTIMATE(8);

private int dbId;

private Rank(int dbId){
	this.dbId = dbId;
}

public int getDbId(){
	return dbId;
}

public static Rank getRank(int dbId){
	if(dbId == 7){
		return EPIC_STAFF;
	}
	Rank[] ranks = Rank.class.getEnumConstants();
	
	for(Rank rank:ranks){
		if(rank.getDbId() == dbId){
			return rank;
		}
	}
	
	return DEFAULT;
}
}