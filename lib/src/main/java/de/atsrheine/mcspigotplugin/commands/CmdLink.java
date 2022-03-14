package de.atsrheine.mcspigotplugin.commands;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;
import de.atsrheine.mcspigotplugin.dcnotifyer.DiscordNotifier;
import de.atsrheine.mcspigotplugin.util.JavaUtil;

public class CmdLink {

	/**
	 * Executes when the channel exist status get retrieved
	 * @param sender the command executor
	 */
	public void onRetreival(CommandSender sender, int status, long guildId, long statusId) {
		
		// Checks the status
		switch(status) {
		// Exists
		case 0:			
			// Updates the bind
			DiscordNotifier.INSTANCE.bindToChannel(guildId, statusId);

			// Sends the info to the sender
			sender.sendMessage(Plugin.PREFIX+" Erfolgreich an den neuen Kanal geknüpft.");
			return;
		// Guild doesn't exist
		case -1:
			// Sends the info to the sender
			sender.sendMessage(Plugin.PREFIX+" §cEs existiert kein Discordserver mit dieser Id. Bitte vergewissere dich mithilfe von \"/dcbot viewchannels\".");
			return;
		// Channel doesn't exist
		case -2:
			// Sends the info to the sender
			sender.sendMessage(Plugin.PREFIX+" §cEs existiert kein Kanal mit dieser Id mit dieser Id. Bitte vergewissere dich mithilfe von \"/dcbot viewchannels\".");
			return;
		}
		
	}
	
	/**
	 * Executes when a failed connection to the discord server's got detected
	 * @param sender the command executor
	 * @param exc the exception of the connection
	 */
	public void onFailedConnection(CommandSender sender, Exception exc) {
		sender.sendMessage(Plugin.PREFIX+" §cFehler bei der Verbindung zum Discord-Server.");
	}
	
    public boolean onCommand(CommandSender sender, String[] args) {
    	
    	// Checks if the argument is invalid
    	if(args.length < 2) {
    		sender.sendMessage(Plugin.PREFIX+" §cSyntax: \"/dcbot link <ServerId>/<KanalId>\". Um alle möglichen Kanäle zu sehen nutze: \"/dcbot viewchannels\"");
    		return true;
    	}
    	
    	// Tries to parse the channel and guild id
    	var split = args[1].split("/");
    	
    	if(split.length < 2) {
    		sender.sendMessage(Plugin.PREFIX+" §cSyntax: \"/dcbot link <ServerId>/<KanalId>\". Um alle möglichen Kanäle zu sehen nutze: \"/dcbot viewchannels\"");
    		return true;
    	}
    	
    	// Tries to parse the elements
    	Long guildId = JavaUtil.parseLong(split[0]);
    	Long channelId = JavaUtil.parseLong(split[1]);
    	
    	// Checks if eigther the channel or guild is not a long
    	if(guildId == null || channelId == null) {
    		sender.sendMessage(Plugin.PREFIX+" §cSyntax: \"/dcbot link <ServerId>/<KanalId>\". Um alle möglichen Kanäle zu sehen nutze: \"/dcbot viewchannels\"");
    		return true;
    	}
    	
    	// Sends info message
    	sender.sendMessage(Plugin.PREFIX+" Versuche den Kanal zu verbinden.");
    	
    	// Tries to connect to the server
    	Plugin.DC_CON.doesTextChannelExistAsync(
			guildId,
			channelId,
			status->this.onRetreival(sender, status,guildId,channelId),
			exc->this.onFailedConnection(sender, exc)
		);
    	
    	return true;
    }
	
}
