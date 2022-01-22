package com.shion1305.lumos.activity_summary;

import com.shion1305.lumos.general.DiscordClientManager;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import reactor.core.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiscordVoiceMonitor {
    List<Long> cache;
    Disposable process;
    Logger logger;

    /**
     * USAGE
     * # create instance and start monitoring
     * DiscordVoiceMonitor discordVoiceMonitor = new DiscordVoiceMonitor();
     * discordVoiceMonitor.start();
     * # call end() if you want to stop monitoring and get result
     * # it passes you an Arraylist of user id as Long.
     * System.out.println(discordVoiceMonitor.end());
     * System.out.print("MONITOR ENDED");
     */

    public DiscordVoiceMonitor() {
        cache = new ArrayList<>();
        logger = Logger.getLogger("DiscordVSMonitor");
    }

    public void start() {
        if (process!=null){
            if (!process.isDisposed())process.dispose();
        }
        process = DiscordClientManager.getClient().on(VoiceStateUpdateEvent.class)
                .filter(event -> event.getCurrent().getGuildId().asLong() == 894226019240800276L)
                .subscribe(voiceStateUpdateEvent -> {
                    VoiceState vs = voiceStateUpdateEvent.getCurrent();
                    Member member = vs.getMember().block();
                    if (!cache.contains(member.getId().asLong())) {
                        cache.add(member.getId().asLong());
                        logger.info("Recorded " + member.getId().asString());
                    }
                });
    }

    public List<Long> end() {
        process.dispose();
        return cache;
    }
}
