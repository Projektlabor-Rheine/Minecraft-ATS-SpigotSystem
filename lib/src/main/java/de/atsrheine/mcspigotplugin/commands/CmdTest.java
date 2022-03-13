package de.atsrheine.mcspigotplugin.commands;

import org.bukkit.command.CommandSender;

import de.atsrheine.mcspigotplugin.Plugin;

public class CmdTest {

	/**
	 * Executes when a successful connection to the discord server's got detected
	 * @param sender the command executor
	 */
	public void onSuccessfullConnection(CommandSender sender) {
		sender.sendMessage(Plugin.PREFIX+" §aVerbindung konnte erfolgreich hergestellt werden.");		
	}
	
	/**
	 * Executes when a failed connection to the discord server's got detected
	 * @param sender the command executor
	 * @param exc the exception of the connection
	 */
	public void onFailedConnection(CommandSender sender, Exception exc) {
		sender.sendMessage(Plugin.PREFIX+" §cVerbindung konnte nicht erfolgreich hergestellt werden. Ist der Bot-token korrekt? Fehlermeldung: "+exc.getMessage());
	}
	
	/**
	 * Command executor
	 * @param sender the sender
	 * @param args the arguments
	 */
    public boolean onCommand(CommandSender sender, String[] args) {
    	// Sends the message that a connection is getting tried
    	sender.sendMessage(Plugin.PREFIX+" Wir testen die Verbindung, Sekunde bitte.");
    	
    	// Tries to connect to the server
    	Plugin.DC_CON.ensureConnectionAsync(
			()->this.onSuccessfullConnection(sender),
			exc->this.onFailedConnection(sender, exc)
    	);
    	
    	return true;
    }
}
