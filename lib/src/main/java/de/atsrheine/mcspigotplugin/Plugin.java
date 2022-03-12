package de.atsrheine.mcspigotplugin;

import org.bukkit.plugin.java.JavaPlugin;

import de.atsrheine.mcspigotplugin.discord.DiscordConnector;
import gitignore.DEBUG_CONSTANTS;

public class Plugin extends JavaPlugin{
	
	// Prefix for the plugin
	public static final String PREFIX = "§8[§cA§aT§bS§7-§7Bot§8]";
	
	// Global discord connection
	// TODO: Load dc-token from file
	public static final DiscordConnector DC_CON = new DiscordConnector(DEBUG_CONSTANTS.DC_TOKEN);
	
	@Override
	public void onEnable() {
		System.out.println("Yooooo läuft");
	}
}
