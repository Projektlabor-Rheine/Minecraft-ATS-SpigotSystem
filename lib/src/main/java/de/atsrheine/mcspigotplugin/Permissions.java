package de.atsrheine.mcspigotplugin;

public class Permissions {
	
	/**
	 * List with all permissions
	 */
	
	public static final String
		PERM_CMD_DCBOT_NOTIFY 			= "atsspigot.dcbot.notify",	// Dcbot notify command
		PERM_CMD_DCBOT_VIEWCHANNELS 	= "atsspigot.dcbot.viewchannels", // Dcbot viewchannels command
		PERM_CMD_DCBOT_BOTTEST 			= "atsspigot.dcbot.bottest" // Dcbot bottest command
	;
	
	// Fixes permissions for the image plugin
	public static String
		PERM_FIX_CMD_IMAGE_CREATE = "images.command.create.url",
		PERM_FIX_CMD_IMAGE_SCALE = "images.command.create.scale";

}
