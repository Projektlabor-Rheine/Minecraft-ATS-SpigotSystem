package de.atsrheine.mcspigotplugin.discord;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

import de.atsrheine.mcspigotplugin.util.FailableThread;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
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
	 * Sync-methods
	 */
	
	/**
	 * Updates the status and the activity of the bot.
	 * 
	 * !Ensures connection automatically!
	 * 
	 * @param status the new status
	 * @param activity the new activity
	 * 
	 * The exception include:
	 * - LoginException
	 * - ErrorResponseException 
	 * 
	 * @return class-instance for method-chaining
	 */
	public DiscordConnector setStatusAndActivity(OnlineStatus status,Activity activity) throws Exception {
		this.ensureConnection();
		// Updates the status
		this.connection.getPresence().setPresence(status, activity);
		
		return this;
	}	
	
	/**
	 * Gets all textchannels that the bot is part of and in all guilds and returns them as string with their ids that are required to uniquely identify them.
	 * 
	 * !Ensures connection automatically!
	 * 
	 * The exception include:
	 * - LoginException
	 * - ErrorResponseException 
	 * 
	 * @return class-instance for method-chaining
	 */
	public ChannelLink[] getTextChannelLinks() throws Exception{
		this.ensureConnection();
		
		// Iterates over all guild and all their text-channels and converts them to simple channel-link objects
		return this.connection.getGuilds().stream().map(guild->{
			return guild.getChannels().stream().filter(channel->channel instanceof TextChannel).map(channel->{
				return new ChannelLink(guild.getName(), channel.getName(), guild.getIdLong(), channel.getIdLong());
			});
		}).flatMap(i->i).toArray(ChannelLink[]::new);
	}
	
	/**
	 * Checks if the bot is on a guild which contains the given channel-id
	 * 
	 * !Ensures connection automatically!
	 * 
	 * The exception include:
	 * - LoginException
	 * - ErrorResponseException 
	 * 
	 * @return the given textchannel if your, otherwise null
	 */
	@Nullable
	public TextChannel getTextChannel(long guildId, long channelId) throws Exception {
		this.ensureConnection();
		
		// Tries to find the guild
		var guild = this.connection.getGuildById(guildId);
		
		if(guild == null)
			return null;
	
		// Tries to find the text-channel
		return guild.getTextChannelById(channelId);
	}
	
	
	
	
	
	
	
	/**
	 * Async-methods
	 */
	
	// Async version
	public DiscordConnector setStatusAndActivityAsync(OnlineStatus status,Activity activity) { return this.setStatusAndActivityAsync(status, activity, null); }
	
	// Async version
	public DiscordConnector setStatusAndActivityAsync(OnlineStatus status,Activity activity, Consumer<Exception> onFail) {
		new FailableThread<Exception>(()->this.setStatusAndActivity(status,activity), onFail).start();
		return this;
	}
	
	// Async version
	public DiscordConnector ensureConnectionAsync(Runnable onSuccess,Consumer<Exception> onFail) {
		new FailableThread<Exception>(()->{
			this.ensureConnection();
			if(onSuccess != null)
				onSuccess.run();
		}, onFail).start();
		return this;
	}
	
	// Async version
	public DiscordConnector getTextChannelLinksAsync(Consumer<ChannelLink[]> onSuccess, Consumer<Exception> onFail) {
		new FailableThread<Exception>(()->{
			onSuccess.accept(this.getTextChannelLinks());
		}, onFail).start();
		return this;
	}
	
	// Async version
	public DiscordConnector doesTextChannelExistAsync(long guildId, long channelId,Consumer<TextChannel> onSuccess, Consumer<Exception> onFail) {
		new FailableThread<Exception>(()->{
			onSuccess.accept(this.getTextChannel(guildId, channelId));
		}, onFail).start();
		return this;
	}
	
	
	
	
	/**
	 * Connection maintain methods
	 */
	
	/**
	 * @return if there is currently an open connection to the discord servers
	 */
	public boolean isConnected() {
		return this.connection != null;
	}

	/**
	 * Sync method to ensure that the bot is connected to discord before doing anything
	 * 
	 * At most user methods of this class this method is automatically called so check there first.
	 * 
	 * @throws LoginException if the token is invalid
	 * @throws ErrorResponseException if the login failed do to a network error
	 * @return the class instance to open the possibility to chain this method with the constructor.
	 */
	public DiscordConnector ensureConnection() throws LoginException,ErrorResponseException {
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
	    
	    // Starts the connection
	    var con = builder.build();
	    
	    // Checks again if in the meantime a new connection had been made
	    if(this.connection != null)
	    	// Kills this connection again to instead use the other one
	    	con.shutdownNow();
	    else {
	    	this.connection = con;
	    	// Waits for the connection to be ready
	    	try {
	    		con.awaitReady();
	    	} catch (InterruptedException e) {}
	    }

	    
	    // (Re)Starts the connection killer thread
	    this.startConnectionKiller();
	    
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
	 * Internal methods
	 */
	
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
