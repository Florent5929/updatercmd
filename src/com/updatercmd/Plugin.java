package com.updatercmd;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	
	public static Logger LOG;
    
	@Override
	public void onEnable() {
		LOG = this.getLogger();
		this.getCommand("updatercmd").setExecutor(new UpdatercmdCommandExecutor());
	}
	
	public static Plugin plugin() {
		return Plugin.getPlugin(Plugin.class);
	}
}