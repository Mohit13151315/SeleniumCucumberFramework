package com.infosys.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config/config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("config.properties file not found");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Config value not found or empty: " + key);
        }
        return value.trim();
    }

    public static String getOptional(String key) {
        String value = PROPERTIES.getProperty(key);
        return value == null ? "" : value.trim();
    }

    public static String getRuntimeValue(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }
        return get(key);
    }

    public static String getEnvironmentUrl() {
        String env = getRuntimeValue("env");
        return get(env + ".url");
    }
}
