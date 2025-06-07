package com.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for handling environment-specific settings
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;
    private String environment;

    private ConfigManager() {
        loadConfiguration();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private void loadConfiguration() {
//        System.setProperty("env","qa");
        properties = new Properties();
        environment = System.getProperty("env", "dev");
        
        String configFileName = "config-" + environment + ".properties";
        
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("Configuration file not found: " + configFileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return getProperty("api.base.url");
    }

    public int getTimeout() {
        return Integer.parseInt(getProperty("api.timeout"));
    }

    public String getEnvironment() {
        return environment;
    }

    public boolean isRetryEnabled() {
        return Boolean.parseBoolean(getProperty("api.retry.enabled"));
    }

    public int getMaxRetries() {
        return Integer.parseInt(getProperty("api.max.retries"));
    }
}