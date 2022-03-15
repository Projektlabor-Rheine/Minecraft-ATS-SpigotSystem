package de.atsrheine.mcspigotplugin.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.atsrheine.mcspigotplugin.Plugin;
import de.atsrheine.mcspigotplugin.dcnotifyer.DiscordNotifier;
import de.atsrheine.mcspigotplugin.dcnotifyer.NotifyType;

public class CmdNotify extends IDcBotCommand{
	
	// Syntax-error
	private final String SYNTAX_ERROR = this.getSyntaxError();
	
	@Override
	public String getName() {
		return "notify";
	}
	
	// Sends a syntax error to the player
	public String getSyntaxError() {
		// Builds a string with all notifyers
		var notifyers = Arrays.stream(NotifyType.values()).map(i->i.getAsString()).collect(Collectors.joining(", "));
		
		return Plugin.PREFIX+" §cSyntax: /dcbot nofity ["+notifyers+"]";
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, String[] args) {		
    	// Checks if a notify-type got passed
		if(args.length < 2) {
			sender.sendMessage(SYNTAX_ERROR);
			return true;
		}
		
		// Tries to parse the notifyer
		var notify = NotifyType.getFromString(args[1]);
    	
		// Checks if it couldn't be found
		if(notify == null) {
			sender.sendMessage(SYNTAX_ERROR);
			return true;
		}
		
		// Ensures that the sender is a player
		if(!(sender instanceof Player)) {
			sender.sendMessage(Plugin.PREFIX+" §cNur Spieler können diesen Befehl ausführen.");
			return true;
		}
		
		// Forwards to the discord-notifyer
		DiscordNotifier.INSTANCE.onPlayerAccept((Player)sender, notify);
		
    	return true;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if(args.length == 2)
			return Arrays.stream(NotifyType.values()).map(i->i.getAsString()).collect(Collectors.toList());
		return null;
	}
}
