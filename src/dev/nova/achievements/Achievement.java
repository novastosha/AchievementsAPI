package dev.nova.achievements;

import com.mysql.fabric.xmlrpc.base.Array;
import dev.nova.achievements.config.AchievementConfigManager;
import dev.nova.achievements.exceptions.AchievementFailedToLoadException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Achievement {

    private YamlConfiguration configuration;
    private File configFile;

    public Achievement(File file){
        try {
            AchievementConfigManager.loadAchievementConfig(file);
            this.configuration = AchievementConfigManager.getConfig();
        } catch (AchievementFailedToLoadException e) {
            e.printStackTrace();
        }
        this.configFile = file;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public ArrayList<String> getDescription(){
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        for(String lines :(ArrayList<String>) configuration.get("description")){
            lore.add(ChatColor.DARK_GRAY+lines);
        }
        return lore;
    }
    public String getDisplayName(){
        return configuration.getString("display-name");
    }
    public void saveConfig(){
        try {
            configuration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUnlockedBy(Player player){
        return getUnlockedList().contains(player.getUniqueId().toString());
    }

    public File getConfigFile() {
        return configFile;
    }
    public ArrayList<String> getUnlockedList(){
        ArrayList<String> list = (ArrayList<String>) configuration.get("players-unlocked");
        if(list == null){
            list = new ArrayList<>();
        }
        return list;
    }
}
