package com.shion1305.lumos.twitter;

import com.shion1305.lumos.general.ConfigManager;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.TimerTask;

public class DMNotifier implements ServletContextListener {
    private static final Twitter twitter;
    private static final Timer timer = new Timer();

    static {
        Configuration conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(ConfigManager.getConfig(ConfigManager.Config.TWITTER_API_KEY))
                .setOAuthConsumerSecret(ConfigManager.getConfig(ConfigManager.Config.TWITTER_SECRET_KEY))
                .setOAuthAccessToken(ConfigManager.getConfig(ConfigManager.Config.TWITTER_ACCESS_TOKEN))
                .setOAuthAccessTokenSecret(ConfigManager.getConfig(ConfigManager.Config.TWITTER_ACCESS_TOKEN_SECRET))
                .build();
        TwitterFactory tf = new TwitterFactory(conf);
        twitter = tf.getInstance();
        try {
            System.out.println(twitter.getOAuthAccessToken().getUserId());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkDM();
            }
        }, 0, 120000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
        timer.purge();
    }

    static void checkDM() {
        try {
            twitter.getDirectMessages(20).forEach(directMessage -> {
                System.out.println(directMessage.getRecipientId());
                System.out.println(directMessage.getSenderId());
                System.out.println(directMessage.getText());
            });
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
