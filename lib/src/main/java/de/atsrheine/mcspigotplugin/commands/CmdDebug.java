package de.atsrheine.mcspigotplugin.commands;

import org.bukkit.command.CommandSender;

public class CmdDebug extends IDcBotCommand{
	
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
    	return true;
    }

    @Override
	public String getName() {
		return "debug";
	}
	
}
