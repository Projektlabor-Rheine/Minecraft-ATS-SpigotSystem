package de.atsrheine.mcspigotplugin.commands;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;
import de.atsrheine.mcspigotplugin.discord.ChannelLink;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CmdViewChannels {

	/**
	 * Executes when the channel got retreived
	 * @param sender the command executor
	 */
	public void onRetreival(CommandSender sender, ChannelLink[] channels) {
		// Builds the message and sends it to the sender
		sender.spigot().sendMessage(this.buildMessage(channels));
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
    
    /**
     * Creates a chatmessage for the player from which he can choose which channel he wants to link the bot to.
     * @param channels the channels that the bot can link to
     * @return the message
     */
    @SuppressWarnings("deprecation")
	private BaseComponent[] buildMessage(ChannelLink[] channels) {
    	var base = new ComponentBuilder();
    	
    	// Iterates over all channels to append them to the message
    	for(var channel : channels) {
    		
    		// Creates the events
    		var hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aKanal §b"+channel.getChannelName()+" §abenutze?"));
    		var click = new ClickEvent(Action.RUN_COMMAND, "/dcbot link "+channel.getGuildId()+"/"+channel.getChannelId());
    		
    		// Creates and appends the message
    		base
    		.appendLegacy(Plugin.PREFIX+" ")
    		.append(channel.getGuildName()).color(ChatColor.GREEN)
    		.append(" => ").color(ChatColor.GRAY)
    		.append(channel.getChannelName()).color(ChatColor.AQUA).event(hover).event(click)
    		.append("\n");
    	}
    	
    	// Appends the final text
    	base.appendLegacy(Plugin.PREFIX+" Um den Bot zu einem der obrigen Textkanäle zu linken, klick einfach auf den, welchen du linken möchtest.");
    	
    	return base.create();
    }
	
}
