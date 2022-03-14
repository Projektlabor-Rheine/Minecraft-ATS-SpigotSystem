package de.atsrheine.mcspigotplugin.dcnotifyer;

import org.bukkit.entity.Player;

import de.atsrheine.mcspigotplugin.Plugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class DiscordNotifier{

	// Join-ask-message
	private final BaseComponent[] JOIN_MESSAGE = this.createJoinMessage();
		
	// Singleton instance
	public static final DiscordNotifier INSTANCE = new DiscordNotifier();
	private DiscordNotifier() {}
	
	// Bound channel and guild
	private long boundGuild,boundChannel;
	
	// Event: When a player joins
	public void onPlayerJoin(Player p) {
		// Sends the join message
		p.spigot().sendMessage(JOIN_MESSAGE);
	}
	
	// Event: When a player quits
	public void onPlayerLeft(Player p) {
		
	}
	
	// Event: A player send wants to accept
	public void onPlayerAccept(Player p,NotifyType type) {
		
	}
	
	// Binds the notifier to the given text channel. This trusted that the channel and guild exist and already got verified.
	public void bindToChannel(long guildId, long channelId) {
		this.boundGuild = guildId;
		this.boundChannel = channelId;
	}
	

	/**
	 * Creates the message that is send to all players when joining to ask them if they want to notify the discord-server of their join.
	 */
	@SuppressWarnings("deprecation")
	private BaseComponent[] createJoinMessage() {
		// Click events
		ClickEvent clickYes = new ClickEvent(Action.RUN_COMMAND, "/say yes");
		ClickEvent clickNo = new ClickEvent(Action.RUN_COMMAND, "/say no");
		ClickEvent clickYesCustomText = new ClickEvent(Action.RUN_COMMAND, "/say yes custom text");
		
		// Hover events
		HoverEvent hoverYes = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aAlle anderen benachrichtigen"));
		HoverEvent hoverNo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§cKeinen benachrichtigen"));
		HoverEvent hoverYesCustomText = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aMit eigener Nachricht."));
		
		// Creates the info message
		return new ComponentBuilder()
			.appendLegacy(
				Plugin.PREFIX+
				" Möchtest du den anderen auf dem Discord mitteilen, dass du jetzt online bist?\n"+
				Plugin.PREFIX+" "
			)
			
			// Yes
			.append("[").color(ChatColor.DARK_GRAY).event(clickYes).event(hoverYes)
			.append("Ja").color(ChatColor.GREEN)
			.append("]").color(ChatColor.DARK_GRAY)
	
			.append(" ").reset()
	
			// Maybe
			.append("[").color(ChatColor.DARK_GRAY).event(clickYesCustomText).event(hoverYesCustomText)
			.append("Eigene Nachricht").color(ChatColor.GREEN)
			.append("]").color(ChatColor.DARK_GRAY)
			.append(" ").reset()
	
			// No
			.append("[").color(ChatColor.DARK_GRAY).event(clickNo).event(hoverNo)
			.append("Ja").color(ChatColor.RED)
			.append("]").color(ChatColor.DARK_GRAY)
			.create();
	}
}
