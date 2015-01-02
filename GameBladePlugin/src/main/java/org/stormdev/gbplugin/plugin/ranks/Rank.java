package org.stormdev.gbplugin.plugin.ranks;

public enum Rank {
DEFAULT(1, org.stormdev.gbapi.cosmetics.Rank.DEFAULT),
VIP(2, org.stormdev.gbapi.cosmetics.Rank.VIP),
VIP_PLUS(3, org.stormdev.gbapi.cosmetics.Rank.VIP_PLUS),
PREMIUM(4, org.stormdev.gbapi.cosmetics.Rank.PREMIUM),
PREMIUM_PLUS(5, org.stormdev.gbapi.cosmetics.Rank.PREMIUM_PLUS),
STAFF(6, org.stormdev.gbapi.cosmetics.Rank.STAFF),
EPIC_STAFF(7, org.stormdev.gbapi.cosmetics.Rank.STAFF),
ULTIMATE(8, org.stormdev.gbapi.cosmetics.Rank.ULTIMATE);

private int dbId;
private org.stormdev.gbapi.cosmetics.Rank cosmeticRank;

private Rank(int dbId, org.stormdev.gbapi.cosmetics.Rank cosmeticRank){
	this.dbId = dbId;
	this.cosmeticRank = cosmeticRank;
}

public org.stormdev.gbapi.cosmetics.Rank getCosmeticRank(){
	return cosmeticRank;
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

public static Rank getRank(org.stormdev.gbapi.cosmetics.Rank rank){
	switch(rank){
	case DEFAULT:
		return Rank.DEFAULT;
	case PREMIUM:
		return Rank.PREMIUM;
	case PREMIUM_PLUS:
		return Rank.PREMIUM_PLUS;
	case STAFF:
		return Rank.EPIC_STAFF;
	case ULTIMATE:
		return Rank.ULTIMATE;
	case VIP:
		return Rank.VIP;
	case VIP_PLUS:
		return Rank.VIP_PLUS;
	default:
		return Rank.DEFAULT;
	}
	//return Rank.DEFAULT;
}
}