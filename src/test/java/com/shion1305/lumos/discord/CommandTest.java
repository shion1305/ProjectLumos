package com.shion1305.lumos.discord;

import com.shion1305.lumos.general.DiscordClientManager;
import org.junit.jupiter.api.Test;

class CommandTest {
    @Test
    void setupDeleteCommand() throws InterruptedException {
    }

    @Test
    void removeAllCommands() throws InterruptedException {
        DiscordClientManager.getClient().getRestClient().getApplicationService()
                .getGlobalApplicationCommands(DiscordClientManager.getApplicationId())
                .subscribe(applicationCommandData -> {
                    System.out.println("CommandDataReceived: " + applicationCommandData.name());
                    DiscordClientManager.getClient().getRestClient()
                            .getApplicationService()
                            .deleteGlobalApplicationCommand(DiscordClientManager.getApplicationId(), Long.parseLong(applicationCommandData.id()))
                            .doOnSuccess(unused -> {
                                System.out.println("SuccessfullyDeleted: " + applicationCommandData.name());
                            }).subscribe();
                });
        Thread.sleep(10000);
    }
}
