package dev.nova.achievements;

import com.mysql.fabric.xmlrpc.base.Array;
import dev.nova.achievements.config.AchievementConfigManager;
import dev.nova.achievements.exceptions.AchievementExistsException;
import dev.nova.achievements.exceptions.AchievementFailedToLoadException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AchievementsAPI {
    public static void createAchievement(String name, String displayName, @Nullable ArrayList<String> descriptionLines) throws AchievementExistsException {
        File file =  new File(Bukkit.getServer().getPluginManager().getPlugin("AchievementsAPI").getDataFolder(),"achievement_"+name+".yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                AchievementConfigManager.loadAchievementConfig(file);
            } catch (AchievementFailedToLoadException e) {
                e.printStackTrace();
            }
            AchievementConfigManager.getConfig().set("display-name",displayName);

            if(descriptionLines == null){
                ArrayList<String> desc = new ArrayList<String>();
                desc.add("No description");
                AchievementConfigManager.getConfig().set("description",desc);
            }else{
                AchievementConfigManager.getConfig().set("description",descriptionLines);

            }
            AchievementConfigManager.saveConfig();
        }else{
            throw new AchievementExistsException("");
        }
    }
    public static void grantAchievement(Player player,Achievement achievement){
        player.sendMessage(ChatColor.GRAY+"Achievement get: "+ChatColor.GREEN+achievement.getDisplayName());
        ArrayList<String> playersUnlocked = (ArrayList<String>) achievement.getConfiguration().get("players-unlocked");
        if(playersUnlocked == null){
            playersUnlocked = new ArrayList<>();
        }
        playersUnlocked.add(player.getUniqueId().toString());
        achievement.getConfiguration().set("players-unlocked",playersUnlocked);
        achievement.saveConfig();
    }
}
