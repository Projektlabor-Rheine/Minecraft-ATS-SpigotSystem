package de.atsrheine.mcspigotplugin.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class IDcBotCommand {

	
	
	// Dc-bot argument used to identify the command
	public abstract String getName();
	
	// Forwarded from the onCommand of the dcbot-command
	public abstract boolean onCommand(CommandSender sender, String[] args);
	
	// Forwarded from the onTabComplete of the dcbot-command
	public List<String> onTabComplete(CommandSender sender, String[] args) { return null; }
	
	// If the command should be shown in tab-complete
	public boolean isTabable() {
		return true;
	}
}
