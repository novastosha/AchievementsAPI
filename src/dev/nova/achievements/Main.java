package dev.nova.achievements;

import dev.nova.achievements.config.AchievementConfigManager;
import dev.nova.achievements.exceptions.AchievementExistsException;
import dev.nova.achievements.exceptions.AchievementFailedToLoadException;
import dev.nova.achievements.gui.AchievementsGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Loading achievements...");
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdirs();
        }
        File defaultConfig = new File(this.getDataFolder(), "config.yml");
        if(!defaultConfig.exists()){
            try {
                defaultConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(this.getConfig().get("players-joined") == null){
            this.getConfig().set("players-joined",0);
        }
        for(File file: this.getDataFolder().listFiles()){
            if(file.getName().startsWith("achievement_")){
                try {
                    AchievementConfigManager.loadAchievementConfig(file);
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Loaded: "+new Achievement(AchievementConfigManager.getConfigFile()).getDisplayName());
                } catch (AchievementFailedToLoadException e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Failed to load: "+file.getName());
                    e.printStackTrace();
                }
            }
        }
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onFirstJoin(PlayerJoinEvent event){
                if(!event.getPlayer().hasPlayedBefore()){
                    Bukkit.getServer().getPluginManager().getPlugin("AchievementsAPI").getConfig().set("players-joined",Bukkit.getServer().getPluginManager().getPlugin("AchievementsAPI").getConfig().getInt("players-joined")+1);
                    Bukkit.getServer().getPluginManager().getPlugin("AchievementsAPI").saveConfig();
                }
            }
        },this);
        this.saveConfig();

    }

    public static float calculatePercentage(ArrayList<String> unlockedList, int playersJoined){
        float percentage = (unlockedList.size() * 100 / playersJoined);
        return percentage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("achievements")){
            AchievementsGUI.openGUI((Player) sender);
        }
        return true;
    }
}
