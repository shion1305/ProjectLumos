package com.shion1305.lumos.discord;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;

public class NoxCommandTest {
    public static void main(String[] args) throws InterruptedException {
        GatewayDiscordClient client = DiscordClient.create(args[0]).gateway().login().block();
        client.on(MessageCreateEvent.class)
                .filter(event -> event.getMessage().getContent().equals("!nox"))
                .subscribe(event -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    var channel = event.getMessage().getChannel().block();
                    channel.createMessage(NoxCommand.createNoxMessage(event.getMember().get())).block();
                });
        Thread.sleep(10000000);
    }
}
