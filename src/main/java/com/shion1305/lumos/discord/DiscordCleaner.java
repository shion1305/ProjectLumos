package com.shion1305.lumos.discord;

import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.gateway.ApplicationCommandCreate;
import reactor.core.Disposable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebListener
public class DiscordCleaner implements ServletContextListener {
    static Disposable disposable;
    Logger logger = Logger.getLogger("DiscordCleaner");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationCommandCreate.builder().command(
                ApplicationCommandData.builder()
                        .applicationId("933021504319422544")

                        .build()
        ).build();
        disposable =
                DiscordClientManager.getClient()
                        .on(MessageCreateEvent.class)
                        .subscribe(messageCreateEvent -> {
                            try {
                                Message message = messageCreateEvent.getMessage();
                                logger.info(message.getContent());
                                Pattern pattern = Pattern.compile("^!!delete ([\\d]+)$");
                                Matcher matcher = pattern.matcher(message.getContent());
                                Optional<Message> ref = messageCreateEvent.getMessage().getReferencedMessage();
                                if (matcher.matches() && messageCreateEvent.getMessage().getReferencedMessage().isPresent() && message.getAuthorAsMember().block().getRoleIds().contains(Snowflake.of(955887232819015720L))) {
                                    int counter = Integer.parseInt(matcher.group(1));
                                    logger.info(String.valueOf(counter));
                                    messageCreateEvent.getMessage().getChannel().subscribe(messageChannel -> {
                                        messageChannel.getMessagesBefore(ref.get().getId())
                                                .take(counter - 1)
                                                .subscribe(message1 -> {
                                                    message1.delete().block();
                                                });
                                    });
                                    ref.get().delete().block();
                                    message.delete().block();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        disposable.dispose();
    }
}
