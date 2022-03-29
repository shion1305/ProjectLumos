package com.shion1305.lumos.general;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.gateway.intent.IntentSet;

public class DiscordClientManager {
    private static final GatewayDiscordClient gateway;
    private static final long applicationId;

    static {
        DiscordClient client = DiscordClient.create(ConfigManager.getConfig("DiscordToken"));
        gateway = DiscordClient.create(ConfigManager.getConfig("DiscordToken")).gateway()
                .setEnabledIntents(IntentSet.all())
                .login().block();
        applicationId = client.getApplicationId().block();
    }

    private DiscordClientManager() {
    }

    public static GatewayDiscordClient getClient() {
        return gateway;
    }

    public static long getApplicationId() {
        return applicationId;
    }
}
