package com.shion1305.lumos.general;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigManager {
    private static Properties config;
    private final static String configDir = System.getProperty("user.home") + "/Lumos/config.properties";

    public static String getConfig(String s) {
        if (config == null) {
            config = new Properties();
            Logger logger = Logger.getLogger("ConfigManager");
            logger.warning("LOADED CONFIG MANAGER");
            try (FileInputStream stream = new FileInputStream(configDir)) {
                config.load(stream);
                logger.warning("LOADED CONFIG MANAGER-1");
            } catch (IOException e) {
                logger.warning("LOADED CONFIG MANAGER-2");
                e.printStackTrace();
            }
        }
        return config.getProperty(s);
    }

}
