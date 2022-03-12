package de.atsrheine.mcspigotplugin.discord;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordConnector {
	
	// Time in seconds until the connection-killer fires his shot at the discord-connection.
	private static final long KILLER_FIRE_DELAY = 5;
	
	// Connection to discord
	private JDA connection;
	
	// Toke for the discord bot
	private String token;
	

	// Scheduler to kill any open connection that idled for too long
    private ScheduledExecutorService connectionKiller = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> killerThread;
	
	public DiscordConnector(String token) {
		this.token = token;
	}
	
	/**
	 * @return if there is currently an open connection to the discord servers
	 */
	public boolean isConnected() {
		return this.connection != null;
	}

	/**
	 * Just ensures that the bot is connected to discord before doing anything
	 * 
	 * At most user methods of this class this method is automatically called so check there first.
	 * 
	 * @throws LoginException if the token is invalid
	 * @return the class instance to open the possibility to chain this method with the constructor.
	 */
	public DiscordConnector ensureConnection() throws LoginException {
		// Checks if the user is connected
		if(this.isConnected()) {
			// Restarts the connection killer thread
			this.startConnectionKiller();
			return this;
		}
		
		// Ensures that any previous connection have been killed
		this.disconnect();
		
		// Creates the builder with the provided token
		JDABuilder builder = JDABuilder.createDefault(this.token);
	    
	    // Disable parts of the cache
	    builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ONLINE_STATUS,CacheFlag.ROLE_TAGS,CacheFlag.VOICE_STATE);
	    
	    // Enables compression
	    builder.setCompression(Compression.ZLIB);
	    
	    // Sets the current activity
	    builder.setActivity(Activity.watching("<your text here>"));
	    
	    // Starts the connection
	    this.connection = builder.build();
	    
	    // Starts the connection killer thread
	    this.startConnectionKiller();
	    
	    return this;
	}
	
	/**
	 * Updates the status and the activity of the bot.
	 * 
	 * !Ensures connection automatically!
	 * 
	 * @param status the new status
	 * @param activity the new activity
	 * @throws LoginException if the connection is required to be reopened and the login-token is invalid.
	 * 
	 * @return class-instance for method-chaining
	 */
	public DiscordConnector setStatusAndActivity(OnlineStatus status,Activity activity) throws LoginException {
		this.ensureConnection();
		
		// Updates the status
		this.connection.getPresence().setPresence(status, activity);
		
		return this;
	}
	
	/**
	 * Force-closes the connection to the discord server
	 */
	public void disconnect() {
		// Checks if the connection is already dead
		if(this.connection == null)
			return;
		
		// Kills the connection
		try {			
			this.connection.shutdownNow();
		}catch(Exception e) {}
		this.connection = null;
	}
	
	/**
	 * (Re)starts the connection killer which kills the discord connection after KILLER_FIRE_DELAY seconds.
	 * If there is already an connection killer, it will be replaced and the delay will be reset
	 */
	private void startConnectionKiller() {
		// Kills the thread
		if(this.killerThread != null && !this.killerThread.isDone() && !this.killerThread.isCancelled())
			this.killerThread.cancel(true);
		
		// Starts the new killer
		this.killerThread = this.connectionKiller.schedule(this::disconnect, KILLER_FIRE_DELAY, TimeUnit.SECONDS);
	}
	
	
}
