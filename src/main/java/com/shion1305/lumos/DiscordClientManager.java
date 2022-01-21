package com.shion1305.lumos;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientPresence;
import discord4j.gateway.intent.IntentSet;

public class DiscordClientManager {
    private static GatewayDiscordClient client;

    public static GatewayDiscordClient getClient() {
        if (client == null) {
            client = DiscordClient.create(ConfigManager.getConfig("DiscordToken")).gateway()
                    .setEnabledIntents(IntentSet.all())
                    .setInitialPresence(s -> ClientPresence.idle()).login().block();
        }
        return client;
    }
}
