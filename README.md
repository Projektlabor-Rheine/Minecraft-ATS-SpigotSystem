# System

Currently the system has the following features:
* Discord-bot that can send a message when a user has joined the discord (If the user wishes to).
* Permissions-fix for the [Image](https://www.spigotmc.org/resources/custom-images.53036/)-Plugin that seperates adds seperate permissions for downloading and scaling images.
* Join/Leave message got changed to a light gray name display with a red minus and green plus.


The system currently only suppoert the german language.

## Discord-bot

On joining the server and having the permissions to send a discord-join message (Permissions that are required for the `/dcbot notify` command) the server sends an info message to the user that askes him if he wants to notify the discord of him joining the server.

### Commands
The discordbot is controlled via the `/dcbot` command.

#### /dcbot bottest
Permissions: `atsspigot.dcbot.bottest`

Requires the `bottoken` to be set in the config.

Tests the plugin's connection to the discord bot.

#### /dcbot help
Permissions: `atsspigot.dcbot.help`

Shows the syntax for the dcbot-commands that the user has permissions for.

#### /dcbot notify <Yes|No>
Permissions: `atsspigot.dcbot.notify`

If specifed with yes, the discordbot sends a join-message to the discord's specified channel. This only works a short amount of time after joining the server. The exact time can be set in the config-section (`dcnotifyer.notify-timeout`).

#### /dcbot viewchannels
Permissions: `atsspigot.dcbot.viewchannels`

Shows all channels that the bot can view in your chat. Clicking on one will copy the required ID that must be input into the config file.

### Config

```yml
# Configurations for the discord-bot
discord:
  # Discord-bot token
  token:
# Configuration for the discord-notification manager
dcnotifyer:
  # Guild- and Channelid for the notification channel
  # View the /dcbot viewchannels command for information on how
  # to get those id's
  guild-id:
  channel-id:
  # How long the user has time after joining to click the notify-button.
  # If he waits longer he/she can no notify the discord.
  # Time in ms (default is 2min = 120000ms)
  notify-timeout: 120000
```


## Image-Plugin fixes

This system got tested with [Image](https://www.spigotmc.org/resources/custom-images.53036/)-Plugin-version: `2.2.5`

The new permissions that are required are:
* To download image from a url: `images.command.create.url`
* To scale an image: `images.command.create.scale`
