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

public class CmdViewChannels extends IDcBotCommand{
	
	@Override
	public String getName() {
		return "viewchannels";
	}
	
	@Override
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
    		var hoverChannel = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aKanal-Id §b"+channel.getChannelName()+" §akopieren?"));
    		var clickChannel = new ClickEvent(Action.COPY_TO_CLIPBOARD, channel.getChannelId()+"");
    		var hoverGuild = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aServer-Id §b"+channel.getGuildName()+" §akopieren?"));
    		var clickGuild = new ClickEvent(Action.COPY_TO_CLIPBOARD, channel.getGuildId()+"");
    		
    		// Creates and appends the message
    		base
    		.appendLegacy(Plugin.PREFIX+" ")
    		.append(channel.getGuildName()).color(ChatColor.GREEN).event(hoverGuild).event(clickGuild)
    		.append(" - ").reset().color(ChatColor.GRAY)
    		.append(channel.getChannelName()).color(ChatColor.AQUA).event(hoverChannel).event(clickChannel)
    		.append("\n");
    	}
    	
    	// Appends the final text
    	base.appendLegacy(
    			Plugin.PREFIX+" Um den Bot zu einem der obrigen Textkanäle zu linken, füge einfach "
    					+ "die Server-Id und die Kanal-Id in die 'config.yml' des Plugins ein. "
    			+ "Diese Id's kannst du per Click auf die jeweiligen Elemente oberhalb kopieren");
    	
    	return base.create();
    }

	
}
