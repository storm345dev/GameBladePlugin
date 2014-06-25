package org.stormdev.gbplugin.api.core;

public class GameBladeAPI implements org.stormdev.gbapi.core.GameBladeAPI{
	private static final double VERSION = 0.1;
	
	@Override
	public double getAPIVersionNumber() {
		return VERSION;
	}

}
