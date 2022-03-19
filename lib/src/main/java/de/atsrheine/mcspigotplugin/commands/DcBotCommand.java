package de.atsrheine.mcspigotplugin.commands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;

public abstract class DcBotCommand {
	
	// The permission that a user needs to execute the given command
	// if null, no permission is needed
	@Nullable
	public String getPermissionsName() { return null; }
	
	// Dc-bot argument used to identify the command
	@Nonnull
	public abstract String getName();
	
	// Forwarded from the onCommand of the dcbot-command
	public abstract void onCommand(CommandSender sender, String[] args);
	
	// Forwarded from the onTabComplete of the dcbot-command
	@Nullable
	public List<String> onTabComplete(CommandSender sender, String[] args) { return null; }
	
	// If the command should be shown in tab-complete
	public boolean isTabable() {
		return true;
	}
	
	// Returns the command syntax for the help command. If null no extra syntax is required
	@Nullable
	public String getSyntax(String... args) {
		return null;
	};
	
	// Sends a syntax error to the command sender
	public void sendSyntaxError(CommandSender sender,String[] args) {
		// Gets the syntax
		var syntax = this.getSyntax(args);
		
		// Builds the error message
		var msg = Plugin.PREFIX+" §cSyntax: /dcbot "+this.getName()+(syntax != null ? " "+syntax : "");
	
		// Sends the message
		sender.sendMessage(msg);
	}
}
