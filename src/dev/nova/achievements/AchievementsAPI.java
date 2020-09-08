package dev.nova.achievements;

import com.mysql.fabric.xmlrpc.base.Array;
import dev.nova.achievements.config.AchievementConfigManager;
import dev.nova.achievements.exceptions.AchievementExistsException;
import dev.nova.achievements.exceptions.AchievementFailedToLoadException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        player.sendMessage(ChatColor.YELLOW+"§kA§r"+ChatColor.GRAY+"- Achievement get: "+ChatColor.YELLOW+achievement.getDisplayName()+ChatColor.GRAY+" -"+ChatColor.YELLOW+" §kA");
        ArrayList<String> playersUnlocked = (ArrayList<String>) achievement.getConfiguration().get("players-unlocked");
        if(playersUnlocked == null){
            playersUnlocked = new ArrayList<>();
        }
        if(!playersUnlocked.contains(player.getUniqueId().toString())) {
            playersUnlocked.add(player.getUniqueId().toString());
            achievement.getConfiguration().set("players-unlocked", playersUnlocked);
            achievement.saveConfig();
        }
    }
}
