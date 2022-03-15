package de.atsrheine.mcspigotplugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.atsrheine.mcspigotplugin.commands.SpigotCMDDcBot;
import de.atsrheine.mcspigotplugin.dcnotifyer.DiscordNotifier;
import de.atsrheine.mcspigotplugin.discord.DiscordConnector;
import de.atsrheine.mcspigotplugin.events.Events;

public class Plugin extends JavaPlugin{
	
	// Prefix for the plugin
	public static final String PREFIX = "§8[§cA§aT§bS§7-§6Bot§8]§7";
	
	// Global discord connection
	public static DiscordConnector DC_CON;
	
	// Tries to load the config stuff
	private boolean loadConfigs() {
		// Gets the config
		FileConfiguration cfg = this.getConfig();
		// Lets the default config be copied
		cfg.options().copyDefaults(true);
		// Saves the default config if no config exists
		this.saveDefaultConfig();
		
		// Load the different module-config's
		
		// General-discord bot stuff (Token, etc.)
		{			
			// Tries to get the bot-token
			var dcToken = cfg.getConfigurationSection("discord").getString("token");
			
			// Checks if the token is set
			if(dcToken == null) {
				Bukkit.getConsoleSender().sendMessage(Plugin.PREFIX+" §cFehler beim laden des Discord-Tokens. Bitte gebe diesen in der 'config.yml' an.");
				return false;
			}
			
			// Inits the discord-connector
			DC_CON = new DiscordConnector(dcToken);
		}
		
		
		// Checks if the loading failes
		if(!DiscordNotifier.INSTANCE.onLoadConfig(cfg))
			return false;
	
		return true;
	}
	
	@Override
	public void onEnable() {
		// Loads the config and check if that didn't work
		if(!this.loadConfigs()) {
			// Disables the plugin
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		// Registers the dcbot command
		this.getCommand("dcbot").setExecutor(new SpigotCMDDcBot());
		
		// Registers the events
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		
		// Starts the discord-notifyer
		DiscordNotifier.INSTANCE.runTaskTimer(this, 0, 20*5);
	}
	
	@Override
	public void onDisable() {
		// Performs a shutdown for the bot if he is connected
		if(DC_CON != null)
			DC_CON.disconnect();
	}
}
