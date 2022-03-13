package de.atsrheine.mcspigotplugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.atsrheine.mcspigotplugin.Plugin;

public class Events implements Listener {

	@EventHandler
	private void onLeave(PlayerQuitEvent evt) {
		// Updates the quit message
		evt.setQuitMessage("§8[§c-§8] §6"+evt.getPlayer().getDisplayName());
	}
	
	@EventHandler
	private void onJoin(PlayerJoinEvent evt) {
		// Updates the join message
		evt.setJoinMessage("§8[§a+§8] §6"+evt.getPlayer().getDisplayName());

		// Sends an example message to the player
		evt.getPlayer().sendMessage(Plugin.PREFIX+" Testmessage");
	}
	
}
