package com.shion1305.lumos.discord;

import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.junit.jupiter.api.Test;

class CommandTest {
    @Test
    void main() throws InterruptedException {
        ApplicationCommandRequest greetCmdRequest = ApplicationCommandRequest.builder()
                .name("delete")
                .description("返信で指定されたコマンドから、指定した件数分、メッセージを一括で削除します。")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("amount")
                        .description("一括で削除するメッセージの数")
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .required(true)
                        .build()
                ).build();
        DiscordClientManager.getClient().getRestClient().getApplicationService()
                .createGlobalApplicationCommand(DiscordClientManager.getApplicationId(), greetCmdRequest)
                .subscribe();
        DiscordClientManager.getClient().on(ApplicationCommandInteractionEvent.class, applicationCommandInteractionEvent -> {
            return applicationCommandInteractionEvent.reply("NYAN! " + applicationCommandInteractionEvent.getInteraction().getMember().get().getUsername());
        }).subscribe();
        Thread.sleep(1000000);
    }
}
