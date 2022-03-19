package de.atsrheine.mcspigotplugin.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.atsrheine.mcspigotplugin.Permissions;
import de.atsrheine.mcspigotplugin.Plugin;
import de.atsrheine.mcspigotplugin.dcnotifyer.DiscordNotifier;
import de.atsrheine.mcspigotplugin.dcnotifyer.NotifyType;

public class CmdNotify extends DcBotCommand{
	
	@Override
	public String getName() {
		return "notify";
	}
	
	@Override
	public String getSyntax(String... args) {
		// Builds a string with all notifiers
		var notifiers = Arrays.stream(NotifyType.values()).map(i->i.getAsString()).collect(Collectors.joining("|"));
		
		return "<"+notifiers+">";
	}

	@Override
	public String getPermissionsName() {
		return Permissions.PERM_CMD_DCBOT_NOTIFY;
	}
	
	
	@Override
	public void onCommand(CommandSender sender, String[] args) {		
    	// Checks if a notify-type got passed
		if(args.length < 2) {
			this.sendSyntaxError(sender, args);
			return;
		}
		
		// Tries to parse the notifyer
		var notify = NotifyType.getFromString(args[1]);
    	
		// Checks if it couldn't be found
		if(notify == null) {
			this.sendSyntaxError(sender, args);
			return;
		}
		
		// Ensures that the sender is a player
		if(!(sender instanceof Player)) {
			sender.sendMessage(Plugin.PREFIX+" §cNur Spieler können diesen Befehl ausführen.");
			return;
		}
		
		// Forwards to the discord-notifyer
		DiscordNotifier.INSTANCE.onPlayerAccept((Player)sender, notify);
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		// Checks for the notifier argument
		if(args.length == 2)
			return Arrays.stream(NotifyType.values()).map(i->i.getAsString()).collect(Collectors.toList());
		return null;
	}
}
