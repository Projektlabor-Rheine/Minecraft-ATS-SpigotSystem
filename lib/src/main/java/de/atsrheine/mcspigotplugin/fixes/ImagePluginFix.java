package de.atsrheine.mcspigotplugin.fixes;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.atsrheine.mcspigotplugin.Permissions;

public class ImagePluginFix {

	
	// Singleton instance
	public static final ImagePluginFix INSTANCE = new ImagePluginFix();
	private ImagePluginFix() {}
	
	// List with command that are used to spawn images
	private List<String> imgCommands = Arrays.asList("customimage","image","images","img");
	
	// This is the same url-test pattern as in the original image plugin: https://github.com/Andavin/Images/blob/master/Images-Core/src/main/java/com/andavin/images/command/CreateCommand.java
	private static final Predicate<String> URL_TEST = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]").asPredicate();
    
	// Event: When a user issues a command
	public void onPlayerUseCommand(PlayerCommandPreprocessEvent evt) {
		
		// Gets the command without slash
		var cmds = evt.getMessage().substring(1).split(" ");
		
		// Checks if the command is in the list of command that are to be checked
		if(!cmds[0].startsWith("images:") && !this.imgCommands.contains(cmds[0].toLowerCase().trim()))
			return;
		
		// Checks if the command is the create command and if there is a uri or image set
		if(cmds.length > 2 && cmds[1].trim().equalsIgnoreCase("create")) {
			
			// Fixes url permissions
			url:{				
				// Checks if the image is not an uri
				if(!URL_TEST.test(cmds[2]))
					break url;
				
				// Checks if the user has the valid permissions
				if(evt.getPlayer().hasPermission(Permissions.PERM_FIX_CMD_IMAGE_CREATE))
					break url;
				
				// Prevents the event
				evt.setCancelled(true);
				
				// Sends the invalid permissions message to the user
				evt.getPlayer().sendMessage("§cDu darfst keine Bilder von einer URL laden.");
			}
			
			// Fixes scalling permissions
			scale:{
				// Checks if a scale is given
				if(cmds.length <= 3)
					break scale;
				
				// Checks if the user has permissions to scale the image
				if(evt.getPlayer().hasPermission(Permissions.PERM_FIX_CMD_IMAGE_SCALE))
					return;

				// Prevents the event
				evt.setCancelled(true);
				
				// Sends the invalid permissions message to the user
				evt.getPlayer().sendMessage("§cDu darfst keine Bilder skalieren.");
			}
		}
	}
	
}
