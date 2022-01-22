package com.shion1305.lumos.activity_summary;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.shion1305.lumos.general.ConfigManager;
import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;

import java.util.concurrent.ExecutionException;

public class SummaryLineSender {
    public static void main(String[] args) throws InterruptedException {
        Guild guild = DiscordClientManager.getClient().getGuildById(Snowflake.of(894226019240800276L)).block();

        guild.getMembers().buffer().subscribe(members -> {
            System.out.println("GOT IT");
            try {
                LineMessagingClient client = LineMessagingClient.builder(ConfigManager.getConfig("LineMessagingToken")).build();
                SummaryLineMessageBuilder mesBuilder = new SummaryLineMessageBuilder("1/24", null, "1/25", "19:00~");
                for (int i = 0; i < 4; i++) {
                    mesBuilder.addMember(members.get(i).getId().asLong());
                }
                PushMessage pushMessage = new PushMessage("U68c3c8e484974b3ca784315d1c2d23ec", mesBuilder.build());
                client.pushMessage(pushMessage).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(10000);
    }
}
