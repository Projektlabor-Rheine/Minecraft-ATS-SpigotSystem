package de.atsrheine.mcspigotplugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.atsrheine.mcspigotplugin.Plugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Events implements Listener {

	@EventHandler
	private void onLeave(PlayerQuitEvent evt) {
		// Updates the quit message
		evt.setQuitMessage("§8[§c-§8] §6"+evt.getPlayer().getDisplayName());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	private void onJoin(PlayerJoinEvent evt) {
		// Updates the join message
		evt.setJoinMessage("§8[§a+§8] §6"+evt.getPlayer().getDisplayName());
		
		// Click events
		ClickEvent clickYes = new ClickEvent(Action.RUN_COMMAND, "/say yes");
		ClickEvent clickNo = new ClickEvent(Action.RUN_COMMAND, "/say no");
		ClickEvent clickYesCustomText = new ClickEvent(Action.RUN_COMMAND, "/say yes custom text");
		
		// Hover events
		HoverEvent hoverYes = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aAlle anderen benachrichtigen"));
		HoverEvent hoverNo = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§cKeinen benachrichtigen"));
		HoverEvent hoverYesCustomText = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aMit eigener Nachricht."));
		
		// Sends the info message to the player
		evt.getPlayer().spigot().sendMessage(
			new ComponentBuilder()
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
			
			.create()
		);		
	}
	
}
