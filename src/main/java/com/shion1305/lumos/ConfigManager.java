package com.shion1305.lumos;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static Properties config;
    private final static String configDir = "config.properties";

    public static String getConfig(String s) {
        if (config == null) {
            config = new Properties();
            try (FileInputStream stream = new FileInputStream(configDir)) {
                config.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config.getProperty(s);
    }

}
