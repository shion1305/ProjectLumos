package com.shion1305.lumos.discord;

import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.Disposable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebListener
public class CommandManager implements ServletContextListener {
    private static List<Long> discordCommands = new ArrayList<>();
    private static final List<Disposable> disposables = new ArrayList<>();
    private static final HashMap<String, Consumer<ChatInputInteractionEvent>> commands = new HashMap<>();
    private static final HashMap<Pattern, Consumer<MessageCreateEvent>> specialCommands = new HashMap<>();


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initiateCommands();
        DiscordClientManager.getClient().getRestClient()
                .getApplicationService()
                .getGlobalApplicationCommands(DiscordClientManager.getApplicationId())
                .map(applicationCommandData -> Long.parseLong(applicationCommandData.id()))
                .collectList()
                .subscribe(longs -> {
                    discordCommands = longs;
                });
    }

    private void initiateCommands() {
        CleanMessageCommand.initiate();
        NoxCommand.initiate();
        disposables.add(
                DiscordClientManager.getClient()
                        .on(ChatInputInteractionEvent.class)
                        .subscribe(CommandManager::handleRequest));
        disposables.add(DiscordClientManager.getClient()
                .on(MessageCreateEvent.class)
                .subscribe(CommandManager::handleSpecialCommandRequest));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        disposables.parallelStream().forEach(Disposable::dispose);
        discordCommands.parallelStream().forEach(aLong -> {
            DiscordClientManager.getClient().getRestClient().getApplicationService()
                    .deleteGlobalApplicationCommand(DiscordClientManager.getApplicationId(), aLong)
                    .subscribe();
        });
    }

    public static void addCommand(String name, Consumer<ChatInputInteractionEvent> consumer) {
        commands.put(name, consumer);
    }

    public static void addSpecialCommand(Pattern regex, Consumer<MessageCreateEvent> consumer) {
        specialCommands.put(regex, consumer);
    }

    private static void handleRequest(ChatInputInteractionEvent event) {
        try {
            commands.get(event.getCommandName()).accept(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleSpecialCommandRequest(MessageCreateEvent event) {
        specialCommands.entrySet().parallelStream().forEach(command -> {
            try {
                Matcher m = command.getKey().matcher(event.getMessage().getContent());
                if (m.matches()) {
                    command.getValue().accept(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
