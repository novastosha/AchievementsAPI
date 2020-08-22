package dev.nova.achievements.config;

import dev.nova.achievements.exceptions.AchievementFailedToLoadException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AchievementConfigManager {

    private static YamlConfiguration configuration;
    private static File configFile;

    public static void loadAchievementConfig(File file) throws AchievementFailedToLoadException {
        configuration = new YamlConfiguration();
        configFile = file;
        try {
            configuration.load(file);
        } catch (IOException e) {
            throw new AchievementFailedToLoadException("File exception!");
        } catch (InvalidConfigurationException e) {
            throw new AchievementFailedToLoadException("Invalid Configuration!");
        }
    }

    public static YamlConfiguration getConfig(){
        if(configuration != null){
            return configuration;
        }
        return null;
    }
    public static void saveConfig(){
        try {
            configuration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getConfigFile() {
        return configFile;
    }
}
