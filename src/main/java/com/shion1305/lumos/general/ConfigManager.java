package com.shion1305.lumos.general;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigManager {
    /**
     * WARNING changed configuration file location.
     */
    private static final Logger logger = Logger.getLogger("ConfigManager");
    private static Properties config;
    private final static String configDir = System.getProperty("user.home") + "/ShionServerConfig/Lumos/config.properties";

    public enum Config {
        DISCORD_TOKEN("DiscordToken"),
        WELCOME_CHANNEL("WelcomeChannel"),
        TARGET_GUILD("TargetGuild"),
        NOX_CHANNEL("NoxChannel"),
        TWITTER_API_KEY("TwitterApiKey"),
        TWITTER_SECRET_KEY("TwitterApiSecret");
        final String name;

        Config(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * REQUIRED PROPERTIES IN config.properties
     * - DiscordToken
     * - WelcomeChannel
     * - TargetGuild
     * - NoxChannel
     */
    public static String getConfig(String field) {
        if (config == null) {
            config = new Properties();
            try (FileInputStream s = new FileInputStream(configDir)) {
                logger.info("Configuration is Loaded");
                config.load(s);
            } catch (IOException e) {
                logger.severe("Configuration LOAD FAILED");
                e.printStackTrace();
            }
        }
        return config.getProperty(field);
    }

    public static String getConfig(Config config) {
        return getConfig(config.getName());
    }
}
