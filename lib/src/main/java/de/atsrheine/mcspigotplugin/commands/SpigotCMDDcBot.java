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
	IDcBotCommand[] commands = new IDcBotCommand[] {
		new CmdDebug(),
		new CmdNotify(),
		new CmdTest(),
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
		var optCmd = Arrays.stream(this.commands).filter(cmd->cmd.getName().equalsIgnoreCase(args[0])).findFirst();
		
		// TODO: Implement help message
		if(optCmd.isEmpty()) {
			sender.sendMessage(Plugin.PREFIX+" Hier könnte ihre Werbung stehen.");
			return true;
		}
		
		// Forwards the command
		optCmd.get().onCommand(sender, args);
				
		return false;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		// Awaits the results
		List<String> results;
		
		// Checks if no arguments are given yet
		if(args.length == 1 || args[0].isEmpty())
			// Returns the subcommands
			results = Arrays.stream(this.commands).filter(cmd->cmd.isTabable()).map(cmd->cmd.getName()).collect(Collectors.toList());
		else {
			// Tries to find the current command
			var optCmd = Arrays.stream(this.commands).filter(cmd->cmd.getName().equalsIgnoreCase(args[0]) && cmd.isTabable()).findFirst();
			
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
