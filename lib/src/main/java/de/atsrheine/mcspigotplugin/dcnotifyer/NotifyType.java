package de.atsrheine.mcspigotplugin.dcnotifyer;

public enum NotifyType {

	YES("Yes"),
	NO("No");
	
	private String asString;
	
	private NotifyType(String asString) {
		this.asString = asString;
	}
	
	public String getAsString() {
		return this.asString;
	}
	
	/**
	 * Tries to find a notifier-type from a given string.
	 * The search is not case sensitive and returns null if no match got found
	 */
	public static NotifyType getFromString(String key) {
		// Iterates over all known types and tries to find a match
		for(var noti : NotifyType.values())
			if(noti.asString.equalsIgnoreCase(key))
				return noti;
		
		return null;
	}
	
}
