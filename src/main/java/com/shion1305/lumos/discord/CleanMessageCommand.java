package com.shion1305.lumos.discord;

import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.common.util.Snowflake;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CleanMessageCommand {
    static final Logger logger = Logger.getLogger("DiscordCleaner");

    private CleanMessageCommand() {

    }

    static synchronized public void initiate() {
        listenCommandDeletion();
        listenNormalDeletion();
    }

    private static void listenCommandDeletion() {
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
                    logger.info("Loading  of command: " + applicationCommandData.name() + " has been completed.");
                })
                .subscribe(applicationCommandData -> {
                    CommandManager.addCommand(applicationCommandData.name(), event -> {
                        long size = event.getOption("size").get().getValue().get().asLong();
                        if (size > 30) {
                            event.reply("削除件数が多すぎます。30件以下にしてください。").withEphemeral(true).block();
                            return;
                        }
                        if (size < 1) {
                            event.reply("件数が無効な数です。").withEphemeral(true).block();
                            return;
                        }
                        event.deferReply().withEphemeral(true).block();
                        Snowflake mesID = event.createFollowup("削除を開始します...").withEphemeral(true).block().getId();
                        event.getInteraction().getChannel().subscribe(messageChannel -> {
                            var ref = new Object() {
                                int count = 0;
                            };
                            messageChannel.getMessagesBefore(messageChannel.getLastMessageId().get())
                                    .take(size).subscribe(message1 -> {
                                        message1.delete().block();
                                        if (++ref.count != size)
                                            event.editFollowup(mesID, InteractionReplyEditSpec.builder()
                                                    .contentOrNull(String.format("IN PROGRESS %02d/%02d", ref.count, size)).build()).block();
                                        else
                                            event.editFollowup(mesID, InteractionReplyEditSpec.builder().contentOrNull(size + "件の削除が完了しました。").build()).block();
                                    });
                        });
                    });
                });
    }

    private static void listenNormalDeletion() {
        CommandManager.addSpecialCommand(Pattern.compile("^!!delete ([\\d]+)$"), messageCreateEvent -> {
            try {
                Message message = messageCreateEvent.getMessage();
                Pattern pattern = Pattern.compile("^!!delete ([\\d]+)$");
                Matcher matcher = pattern.matcher(message.getContent());
                Optional<Message> ref = messageCreateEvent.getMessage().getReferencedMessage();
                if (matcher.matches() && messageCreateEvent.getMessage().getReferencedMessage().isPresent() && message.getAuthorAsMember().block().getRoleIds().contains(Snowflake.of(955887232819015720L))) {
                    int counter = Integer.parseInt(matcher.group(1));
                    if (counter < 31 && counter > 0) {
                        messageCreateEvent.getMessage().getChannel().subscribe(messageChannel -> {
                            messageChannel.getMessagesBefore(ref.get().getId())
                                    .take(counter - 1)
                                    .subscribe(message1 -> {
                                        message1.delete().block();
                                    });
                        });
                        ref.get().delete().block();
                        message.delete().block();
                    } else {
                        messageCreateEvent.getMessage().getChannel().subscribe(messageChannel -> {
                            messageChannel.createMessage("削除件数は1~30の範囲で有効です。").block();
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
