package de.atsrheine.mcspigotplugin.commands;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;

public class CmdDebug {
	
	public void onSuccessfullConnection(CommandSender sender) {
		sender.sendMessage(Plugin.PREFIX+" §aVerbindung konnte erfolgreich hergestellt werden.");		
	}
	
	public void onFailedConnection(CommandSender sender, Exception exc) {
		sender.sendMessage(Plugin.PREFIX+" §cVerbindung konnte nicht erfolgreich hergestellt werden. Ist der Bot-token korrekt? Fehlermeldung: "+exc.getMessage());
	}
	
    public boolean onCommand(CommandSender sender, String[] args) {
    	sender.sendMessage(Plugin.PREFIX+" Wir testen die Verbindung, Sekunde bitte.");
    	
    	// Tries to connect to the server
    	Plugin.DC_CON.ensureConnectionAsync(
			()->this.onSuccessfullConnection(sender),
			exc->this.onFailedConnection(sender, exc)
    	);
    	
    	return true;
    }

	
}
