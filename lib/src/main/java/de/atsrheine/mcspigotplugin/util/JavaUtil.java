package de.atsrheine.mcspigotplugin.util;

public class JavaUtil {

	/**
	 * Tries to parse a long from a given string.
	 * @param str the string that is presumed to be a long
	 * @return Null if the given string couldn't be parsed, otherwise the long
	 */
	public static Long parseLong(String str) {
		try {
			return Long.parseLong(str);
		}catch(NumberFormatException e) {
			return null;
		}
	}
	
}
