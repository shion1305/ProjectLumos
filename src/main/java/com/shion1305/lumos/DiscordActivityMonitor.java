package com.shion1305.lumos;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.presence.ClientPresence;

import java.util.logging.Logger;

public class DiscordActivityMonitor  {
    Logger logger;
    public static void main(String[] args) throws InterruptedException {
        GatewayDiscordClient client = DiscordClient.create(ConfigManager.getConfig("DiscordToken")).login().block();
        client.updatePresence(ClientPresence.invisible()).subscribe();
        client.on(VoiceStateUpdateEvent.class)
                .filter(voiceChannelUpdateEvent -> voiceChannelUpdateEvent.getCurrent().getGuildId().asLong() == 894226019240800276L)
                .subscribe(voiceChannelUpdateEvent -> {
                    VoiceState vs = voiceChannelUpdateEvent.getCurrent();
                    Member member=vs.getMember().block();
                    System.out.println("EVENT RECEIVED");
                    System.out.println(member.getId().asLong());
                    System.out.println(member.getAvatarUrl());
                });
        Thread.sleep(100000000);
    }

//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        if (logger==null){
//            logger=Logger.getLogger("LumosDiscordActivityMonitor");
//        }
//        logger.severe("SEVERE=TEST");
//        logger.info("INFO=TEST");
//        GatewayDiscordClient client = DiscordClient.create("ODU5MTExNzM4ODA4ODYwNjgz.YNn8KA.KUhUktJiR3uyk3waEdqTlWsd3aQ").login().block();
//        client.updatePresence(ClientPresence.invisible()).subscribe();
//        client.on(VoiceStateUpdateEvent.class)
//                .filter(voiceChannelUpdateEvent -> voiceChannelUpdateEvent.getCurrent().getGuildId().asLong() == 894226019240800276L)
//                .subscribe(voiceChannelUpdateEvent -> {
//                    VoiceState vs = voiceChannelUpdateEvent.getCurrent();
//                    Member member=vs.getMember().block();
//                    logger.info("Event Detected");
//                    logger.info("TargetUser:"+member.getId().asString());
//                });
//    }
}
