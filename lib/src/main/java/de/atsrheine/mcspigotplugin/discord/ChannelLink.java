package de.atsrheine.mcspigotplugin.discord;

public class ChannelLink {
	
	private String guildName, channelName;
	private long guildId, channelId;
	
	public ChannelLink(String guildName, String channelName, long guildId, long channelId) {
		this.guildId = guildId;
		this.guildName = guildName;
		this.channelId = channelId;
		this.channelName = channelName;
	}
	
	public long getChannelId() {
		return channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public long getGuildId() {
		return guildId;
	}
	public String getGuildName() {
		return guildName;
	}
	
}
