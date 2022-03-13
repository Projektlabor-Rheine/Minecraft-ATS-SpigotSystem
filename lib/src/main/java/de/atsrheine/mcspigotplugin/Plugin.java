package de.atsrheine.mcspigotplugin;

import org.bukkit.plugin.java.JavaPlugin;

import de.atsrheine.mcspigotplugin.commands.SpigotCMDDcBot;
import de.atsrheine.mcspigotplugin.discord.DiscordConnector;
import gitignore.DEBUG_CONSTANTS;

public class Plugin extends JavaPlugin{
	
	// Prefix for the plugin
	public static final String PREFIX = "§8[§cA§aT§bS§7-§6Bot§8]§7";
	
	// Global discord connection
	// TODO: Load dc-token from file
	public static final DiscordConnector DC_CON = new DiscordConnector(DEBUG_CONSTANTS.DC_TOKEN);
	
	@Override
	public void onEnable() {
		// Registers the dcbot command
		this.getCommand("dcbot").setExecutor(new SpigotCMDDcBot());
	}
	
	@Override
	public void onDisable() {
		// Performs a shutdown for the bot if he is connected
		DC_CON.disconnect();
	}
}
