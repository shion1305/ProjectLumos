package com.shion1305.lumos.welcomer;

import com.shion1305.lumos.general.ConfigManager;
import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.entity.RestChannel;
import discord4j.rest.util.Color;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Objects;

@WebListener
public class WelcomeSender implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GatewayDiscordClient client = DiscordClientManager.getClient();
        RestChannel channel = Objects.requireNonNull(client.getChannelById(Snowflake.of(ConfigManager.getConfig("WelcomeChannel"))).block()).getRestChannel();
        client.on(MemberJoinEvent.class)
                .filter(memberJoinEvent -> !memberJoinEvent.getMember().isBot())
                .subscribe(memberJoinEvent -> {
                    Member member = memberJoinEvent.getMember();
                    channel.createMessage(EmbedCreateSpec.builder()
                                    .title("Lumosへようこそ!!!")
                                    .description("<@" + member.getId().asLong() + ">さんがLumosにやってきました:sparkles:")
                                    .author(EmbedCreateFields.Author.of(member.getUsername(), null, member.getAvatarUrl()))
                                    .color(Color.MOON_YELLOW)
                                    .build().asRequest())
                            .block();
                });
    }
}
