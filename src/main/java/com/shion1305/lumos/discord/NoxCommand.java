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
    private static Channel channel;
    private static final String[] msg = new String[]{"お疲れ!", "Goodbye! See you again!!", "See ya!", "またな!", "printf(\"Goodbye, world!\");", "続けるにはENTERを押すかコマンドを入力してください", "terminated with status code 0", "Console.WriteLine(\"Goodbye, world!\");"};
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger("NoxCommand");

    private NoxCommand() {

    }

    synchronized static void initiate() {
        channel = DiscordClientManager.getClient().getChannelById(Snowflake.of(ConfigManager.getConfig(ConfigManager.Config.NOX_CHANNEL)))
                .block();
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
                            event.deferReply().withEphemeral(true).block();
                            Member member = event.getInteraction().getMember().get();
                            if (member.getVoiceState().block() != null) {
                                channel.getRestChannel().createMessage(createNoxMessage(member).asRequest()).block();
                                event.createFollowup("Noxコマンドを発動しました! お疲れ!").block();
                            } else {
                                event.createFollowup("このコマンドはボイスチャンネルに入っている時のみ有効です。").block();
                            }
                        }));
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
