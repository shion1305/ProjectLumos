package com.shion1305.lumos.discord;

import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.Disposable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebListener
public class DiscordCleaner implements ServletContextListener {
    static List<Disposable> disposable = new ArrayList<>();
    static final Logger logger = Logger.getLogger("DiscordCleaner");

    static List<Long> discordCommands = DiscordClientManager.getClient().getRestClient()
            .getApplicationService()
            .getGlobalApplicationCommands(DiscordClientManager.getApplicationId())
            .map(applicationCommandData -> Long.parseLong(applicationCommandData.id()))
            .collectList()
            .block();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        listenCommandDeletion();
        listenNormalDeletion();
    }

    private void listenCommandDeletion() {
        ApplicationCommandRequest request = ApplicationCommandRequest.builder()
                .name("delete")
                .description("先頭から指定した数のメッセージを削除します。")
                .type(ApplicationCommand.Type.CHAT_INPUT.getValue())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("size")
                        .description("削除するメッセージの数")
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .required(true)
                        .build())
                .build();
        DiscordClientManager.getClient().getRestClient().getApplicationService()
                .createGlobalApplicationCommand(DiscordClientManager.getApplicationId(), request)
                .doOnError(throwable -> {
                    logger.severe("Error in deletion command setup sequence.");
                    throwable.printStackTrace();
                })
                .doOnSuccess(applicationCommandData -> {
                    logger.info("Loading  of command: "+applicationCommandData.name()+" has been completed.");
                })
                .subscribe();
        disposable.add(DiscordClientManager.getClient()
                .on(ChatInputInteractionEvent.class)
                .subscribe(chatInputInteractionEvent -> {
                    long size = chatInputInteractionEvent.getOption("size").get().getValue().get().asLong();
                    if (size > 30) {
                        chatInputInteractionEvent.reply("削除件数が多すぎます。30件以下にしてください。").withEphemeral(true).block();
                        return;
                    }
                    if (size < 1) {
                        chatInputInteractionEvent.reply("件数が無効な数です。").withEphemeral(true).block();
                        return;
                    }
                    chatInputInteractionEvent.deferReply().withEphemeral(true).block();
                    Snowflake mesID = chatInputInteractionEvent.createFollowup("削除を開始していました...").withEphemeral(true).block().getId();
                    chatInputInteractionEvent.getInteraction().getChannel().subscribe(messageChannel -> {
                        var ref = new Object() {
                            int count = 0;
                        };
                        messageChannel.getMessagesBefore(messageChannel.getLastMessageId().get())
                                .take(size).subscribe(message1 -> {
                                    message1.delete().block();
                                    if (++ref.count != size)
                                        chatInputInteractionEvent.editFollowup(mesID, InteractionReplyEditSpec.builder()
                                                .contentOrNull(String.format("IN PROGRESS %02d/%02d", ref.count, size)).build()).block();
                                    else
                                        chatInputInteractionEvent.editFollowup(mesID, InteractionReplyEditSpec.builder().contentOrNull(size + "件の削除が完了しました。").build()).block();
                                });
                    });
                }));
    }

    private void listenNormalDeletion() {
        disposable.add(
                DiscordClientManager.getClient()
                        .on(MessageCreateEvent.class)
                        .subscribe(messageCreateEvent -> {
                            try {
                                Message message = messageCreateEvent.getMessage();
                                Pattern pattern = Pattern.compile("^!!delete ([\\d]+)$");
                                Matcher matcher = pattern.matcher(message.getContent());
                                Optional<Message> ref = messageCreateEvent.getMessage().getReferencedMessage();
                                if (matcher.matches() && messageCreateEvent.getMessage().getReferencedMessage().isPresent() && message.getAuthorAsMember().block().getRoleIds().contains(Snowflake.of(955887232819015720L))) {
                                    int counter = Integer.parseInt(matcher.group(1));
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
                        }));
    }

    private void removeCommand(Long id) {
        DiscordClientManager.getClient().getRestClient()
                .getApplicationService()
                .deleteGlobalApplicationCommand(DiscordClientManager.getApplicationId(), id)
                .subscribe();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        disposable.forEach(Disposable::dispose);
        discordCommands.forEach(this::removeCommand);
    }
}
