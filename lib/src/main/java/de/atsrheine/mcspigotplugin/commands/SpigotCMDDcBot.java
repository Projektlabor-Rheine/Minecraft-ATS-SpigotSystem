package de.atsrheine.mcspigotplugin.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import de.atsrheine.mcspigotplugin.Plugin;

public class SpigotCMDDcBot implements CommandExecutor, TabCompleter{
	
	// List with registered-commands
	DcBotCommand[] commands = new DcBotCommand[] {
		// Important help message must be first index
			
		new CmdNotify(),
		new CmdBotTest(),
		new CmdViewChannels()
	};
	
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
		// Checks if no arguments got given
		if(args.length <= 0) {
			// TODO: Implement help message
			sender.sendMessage(Plugin.PREFIX+"Hier könnte ihre Werbung stehen");
			return false;
		}
		
		// Tries to find a matching command
		var optCmd = Arrays.stream(this.commands)
				// Searches the name
				.filter(cmd->cmd.getName().equalsIgnoreCase(args[0])).findFirst();
		
		// TODO: Implement help message
		if(optCmd.isEmpty()) {
			sender.sendMessage(Plugin.PREFIX+" Hier könnte ihre Werbung stehen.");
			return true;
		}

		// Gets the command
		var cmd = optCmd.get();
		
		// Checks if the user has permissions for that command
		if(cmd.getPermissionsName() != null && !sender.hasPermission(cmd.getPermissionsName())) {
			sender.sendMessage(Plugin.PREFIX+" §cDu bist nicht berechtigt um diesen Befehl zu verwenden.");
			return true;
		}
		
		// Forwards the command
		cmd.onCommand(sender, args);
				
		return false;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		// Awaits the results
		List<String> results;
		
		// Checks if no arguments are given yet
		if(args.length == 1 || args[0].isEmpty())
			// Returns the subcommands
			results = Arrays.stream(this.commands)
				// Searches only tabable commands
				.filter(cmd->cmd.isTabable())
				// Searches only commands that the sender has permissions to
				.filter(cmd->cmd.getPermissionsName() == null || sender.hasPermission(cmd.getPermissionsName()))
				.map(cmd->cmd.getName())
				.collect(Collectors.toList());
		else {
			// Tries to find the current command
			var optCmd = Arrays.stream(this.commands)
				// Searches only tabable commands
				.filter(cmd->cmd.isTabable())
				// Searches only commands that the sender has permissions to
				.filter(cmd->cmd.getPermissionsName() == null || sender.hasPermission(cmd.getPermissionsName()))
				// Searches for the name
				.filter(cmd->cmd.getName().equalsIgnoreCase(args[0]) && cmd.isTabable())
				.findFirst();
			
			// Checks if there is a command
			if(optCmd.isEmpty())
				return new ArrayList<>();
			
			// Fills the results
			results = optCmd.get().onTabComplete(sender, args);
			
			// Checks if any tab-complete got found
			if(results == null)
				return new ArrayList<>();
		}
		

		// Creates the list with the returns
		var ls = new ArrayList<String>();
		
		// Filters the results 
		StringUtil.copyPartialMatches(args[args.length-1], results, ls);
        Collections.sort(ls);
        
		return ls;
	}

}
