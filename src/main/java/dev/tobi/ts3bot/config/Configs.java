package dev.tobi.ts3bot.config;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Configs {

    private static final String CONFIG_FILE = "config.properties";

    private static Properties readConfigFromFile() {
        Properties config = new Properties();
        try {
            File configFile = new File(CONFIG_FILE);
            if (!configFile.exists()) {
                configFile.createNewFile();
            }

            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            config.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public void writeConfigToFile(Properties config) {
        try {
            File configFile = new File(CONFIG_FILE);
            if(!configFile.exists()) {
                configFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            config.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return readConfigFromFile();
    }
}
