package de.atsrheine.mcspigotplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;

public class SpigotCMDDcBot implements CommandExecutor{
	
	// Command instances
	private CmdDebug cmdDebug = new CmdDebug();
	private CmdTest cmdTest = new CmdTest();
	private CmdViewChannels cmdViewChannels = new CmdViewChannels();
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
		// Checks if no arguments got given
		if(args.length <= 0) {
			// TODO: Implement help message
			sender.sendMessage(Plugin.PREFIX+"Hier könnte ihre Werbung stehen");
			return false;
		}
		
		// Checks for the argument
		switch(args[0].toLowerCase()) {
			case "debug": return cmdDebug.onCommand(sender, args);
			case "test": return cmdTest.onCommand(sender, args);
			case "viewchannels": return cmdViewChannels.onCommand(sender, args);
		}

		// TODO: Implement help message
		sender.sendMessage(Plugin.PREFIX+" Hier könnte ihre Werbung stehen.");
		
		return false;
    }

}
