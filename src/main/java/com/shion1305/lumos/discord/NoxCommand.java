package com.shion1305.lumos.discord;

import com.shion1305.lumos.general.ConfigManager;
import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.common.util.Snowflake;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;

import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

public class NoxCommand {
    private static final String[] msg = new String[]{"お疲れ!", "Goodbye! See you again!!", "See ya!", "またな!", "printf(\"Goodbye, world!\");", "続けるにはENTERを押すかコマンドを入力してください", "terminated with status code 0", "Console.WriteLine(\"Goodbye, world!\");"};
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger("NoxCommand");

    private NoxCommand() {

    }

    synchronized static void initiate() {
        ApplicationCommandRequest request = ApplicationCommandRequest.builder()
                .type(ApplicationCommand.Type.CHAT_INPUT.getValue())
                .name("nox")
                .description("疲れたのかい？ならばnoxだ！")
                .build();
        DiscordClientManager.getClient().getRestClient().getApplicationService()
                .createGlobalApplicationCommand(DiscordClientManager.getApplicationId(), request)
                .doOnSuccess(applicationCommandData ->
                        logger.info("NoxCommand has been mounted"))
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                    logger.warning("NoxCommand failed to mount");
                })
                .subscribe(data ->
                        CommandManager.addCommand(data.name(), event -> {
                            try {
                                event.deferReply().withEphemeral(true).block();
                                Optional<Member> o_member = event.getInteraction().getMember();
                                if (o_member.isEmpty()) {
                                    logger.warning("NoxCommand failed to get member");
                                    return;
                                }
                                Member member = o_member.get();
                                Channel channel = event.getInteraction().getChannel().block();
                                if (member.getVoiceState().block() != null) {
                                    assert channel != null;
                                    channel.getRestChannel().createMessage(createNoxMessage(member).asRequest()).block();
                                    event.createFollowup("Noxコマンドを発動しました! お疲れ!").block();
                                } else {
                                    event.createFollowup("このコマンドはボイスチャンネルに入っている時のみ有効です。").block();
                                }
                            } catch (Exception e) {
                                logger.severe("ERROR in Nox command...");
                                e.printStackTrace();
                            }
                        })
                );
    }

    public static EmbedCreateSpec createNoxMessage(Member member) {
        return EmbedCreateSpec.builder()
                .color(Color.MOON_YELLOW)
                .author(EmbedCreateFields.Author.of(member.getDisplayName(), member.getAvatarUrl(), member.getAvatarUrl()))
                .description(member.getUsername() + " さんが退出します!  " + "<@" + member.getId().asLong() + ">")
                .footer(EmbedCreateFields.Footer.of(msg[random.nextInt(msg.length)], null))
                .build();
    }
}
