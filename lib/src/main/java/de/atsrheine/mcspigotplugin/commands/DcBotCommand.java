package de.atsrheine.mcspigotplugin.commands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

public abstract class DcBotCommand {
	
	// The permission that a user needs to execute the given command
	// if null, no permission is needed
	@Nullable
	public String getPermissionsName() { return null; }
	
	// Dc-bot argument used to identify the command
	@Nonnull
	public abstract String getName();
	
	// Forwarded from the onCommand of the dcbot-command
	public abstract boolean onCommand(CommandSender sender, String[] args);
	
	// Forwarded from the onTabComplete of the dcbot-command
	@Nullable
	public List<String> onTabComplete(CommandSender sender, String[] args) { return null; }
	
	// If the command should be shown in tab-complete
	public boolean isTabable() {
		return true;
	}
}
