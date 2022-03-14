package de.atsrheine.mcspigotplugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.atsrheine.mcspigotplugin.dcnotifyer.DiscordNotifier;

public class Events implements Listener {

	@EventHandler
	private void onLeave(PlayerQuitEvent evt) {
		// Updates the quit message
		evt.setQuitMessage("§8[§c-§8] §6"+evt.getPlayer().getDisplayName());
	
		// Sends the event to the discord-notifyer
		DiscordNotifier.INSTANCE.onPlayerLeft(evt.getPlayer());
	}
	
	@EventHandler
	private void onJoin(PlayerJoinEvent evt) {
		// Updates the join message
		evt.setJoinMessage("§8[§a+§8] §6"+evt.getPlayer().getDisplayName());
		
		// Sends the event to the discord-notifyer
		DiscordNotifier.INSTANCE.onPlayerJoin(evt.getPlayer());
	}
	
}
