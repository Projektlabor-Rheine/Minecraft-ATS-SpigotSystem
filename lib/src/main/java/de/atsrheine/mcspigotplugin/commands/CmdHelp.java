package de.atsrheine.mcspigotplugin.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Permissions;
import de.atsrheine.mcspigotplugin.Plugin;

public class CmdHelp extends DcBotCommand{

	// Ref to all registered commands
	private DcBotCommand[] commands;
	
	// Inits the help command with all commands that it must be awair of
	public void init(DcBotCommand[] commands) {
		this.commands = commands;
	}
	
	
	@Override
	public String getName() {
		return "help";
	}
	
	@Override
	public String getSyntax(String... args) {
		return "<Befehl>";
	}
	
	@Override
	public String getPermissionsName() {
		return Permissions.PERM_CMD_DCBOT_HELP;
	}
	
	
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		// Checks if no command got send
		if(args.length < 2) {
			this.sendSyntaxError(sender, args);
			return;
		}
		
		// Tries to find the command to find help for
		var optCommand = Arrays.stream(this.commands)
				.filter(cmd->cmd.getName().equalsIgnoreCase(args[1]))
				.findFirst();
		
		// Checks if the command exists
		if(optCommand.isEmpty()) {
			sender.sendMessage(Plugin.PREFIX+" §cBefehl wurde nicht gefunden.");
			return;
		}
		
		// Gets the command
		var command = optCommand.get();
		
		// Checks if the user has permissions for that command
		if(!sender.hasPermission(command.getPermissionsName())) {
			sender.sendMessage(Plugin.PREFIX+" §cAuf diesen Befehl hast du keinen Zugriff.");
			return;
		}
		
		// Gets the syntax
		var syntax = command.getSyntax();
		
		// Displays the syntax
		sender.sendMessage(Plugin.PREFIX+" Syntax: /dcbot "+command.getName()+(syntax == null ? "" : (" "+syntax)));
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		// Checks for the command-argument
		if(args.length == 2)
			// Searches for all command which the user is allowed to execute
			return Arrays.stream(this.commands)
			// Searches only tabable commands
			.filter(cmd->cmd.isTabable())
			// Searches only commands that the sender has permissions to
			.filter(cmd->cmd.getPermissionsName() == null || sender.hasPermission(cmd.getPermissionsName()))
			.map(cmd->cmd.getName())
			.collect(Collectors.toList());
			
		return null;
	}
	
}
