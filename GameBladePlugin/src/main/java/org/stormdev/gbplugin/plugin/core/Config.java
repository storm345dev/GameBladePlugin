package org.stormdev.gbplugin.plugin.core;

import java.io.File;

import org.stormdev.gbapi.config.ConfigBase;
import org.stormdev.gbapi.config.ConfigSetting;

public class Config extends ConfigBase {
	public static ConfigSetting<Boolean> colouredLogger = new ConfigSetting<Boolean>("general.logger.colour", true);
	
	public Config(File file){
		super(file);
		load();
	}
}
