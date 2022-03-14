package de.atsrheine.mcspigotplugin.commands;

import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;
import de.atsrheine.mcspigotplugin.dcnotifyer.DiscordNotifier;
import de.atsrheine.mcspigotplugin.util.JavaUtil;
import net.dv8tion.jda.api.entities.TextChannel;

public class CmdLink {

	/**
	 * Executes when the channel exist status get retrieved
	 * @param sender the command executor
	 */
	public void onRetreival(CommandSender sender, @Nullable TextChannel channel) {
		
		// Checks if channel couldn't be found
		if(channel == null) {
			// Sends the info to the sender
			sender.sendMessage(Plugin.PREFIX+" §cEs existiert kein Discordserver oder kein Kanal mit dieser Id. Bitte vergewissere dich mithilfe von \"/dcbot viewchannels\".");
			return;
		}
			
		// Updates the bind
		DiscordNotifier.INSTANCE.bindToChannel(channel.getGuild().getIdLong(), channel.getIdLong());

		// Sends the info to the sender
		sender.sendMessage(Plugin.PREFIX+" §aErfolgreich §7an den neuen Kanal geknüpft.");
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
			channel->this.onRetreival(sender, channel),
			exc->this.onFailedConnection(sender, exc)
		);
    	
    	return true;
    }
	
}
