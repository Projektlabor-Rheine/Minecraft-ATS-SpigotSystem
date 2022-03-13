package de.atsrheine.mcspigotplugin.commands;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;
import de.atsrheine.mcspigotplugin.discord.ChannelLink;

public class CmdViewChannels {

	/**
	 * Executes when the channel got retreived
	 * @param sender the command executor
	 */
	public void onRetreival(CommandSender sender, ChannelLink[] channels) {
		// Creates the message with all channels.
		var message = Arrays.stream(channels).map(channel->{
			return Plugin.PREFIX+String.format(
				" §a%s§8/§b%s§7 => §a%d§8/§b%d",
				channel.getGuildName(),
				channel.getChannelName(),
				channel.getGuildId(),
				channel.getChannelId());
		}).collect(Collectors.joining("\n"));
		
		// Builds the message and sends it to the sender
		sender.sendMessage(message+"\n"+Plugin.PREFIX+" Die obrigen Textkanäle befinden sich in folgendem Format: §a<Server>§8/§b<Kanal>§7 => §a<SID>§8/§b<KID>§7. Um den Bot auf einen bestimmten Kanal zu linken, nutze bitte: \"/dcbot link §a<SID>§8/§b<KID>\"§7.");
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
    	sender.sendMessage(Plugin.PREFIX+" Lade alle Textkanäle vom Bot.");
    	
    	// Tries to connect to the server
    	Plugin.DC_CON.getTextChannelLinksAsync(
			channels->this.onRetreival(sender, channels),
			exc->this.onFailedConnection(sender, exc)
		);
    	
    	return true;
    }
	
}
