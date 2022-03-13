package de.atsrheine.mcspigotplugin.util;

@FunctionalInterface
public interface FailCallback<T extends Exception>{
	public void run() throws T;
}
