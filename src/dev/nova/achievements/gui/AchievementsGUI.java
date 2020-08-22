package dev.nova.achievements.gui;

import com.mysql.fabric.xmlrpc.base.Array;
import dev.nova.achievements.Achievement;
import dev.nova.achievements.Main;
import dev.nova.achievements.config.AchievementConfigManager;
import dev.nova.achievements.exceptions.AchievementFailedToLoadException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

public class AchievementsGUI {


    public static void openGUI(Player player){
        Inventory inv = Bukkit.createInventory(player, 54,"§7Achievements");
        Plugin plugin = Main.getPlugin(Main.class);
        int inSlot = 0;
        for(File file: plugin.getDataFolder().listFiles()){
            if(file.getName().startsWith("achievement_")){
                try {
                    AchievementConfigManager.loadAchievementConfig(file);
                    Achievement achievement = new Achievement(AchievementConfigManager.getConfigFile());
                    ArrayList<String> lore = achievement.getDescription();
                    lore.add("   ");
                    lore.add(ChatColor.GRAY+"Unlocked by: "+Main.calculatePercentage(achievement.getUnlockedList(),plugin.getConfig().getInt("players-joined"))+"%");
                    if(!achievement.isUnlockedBy(player)){
                        ItemStack item = new ItemStack(Material.REDSTONE);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setLore(lore);
                        itemMeta.setDisplayName(ChatColor.RED+achievement.getDisplayName());
                        item.setItemMeta(itemMeta);
                        inv.setItem(inSlot,item);
                        inSlot++;
                    }else{
                        ItemStack item = new ItemStack(Material.EMERALD);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setLore(lore);
                        itemMeta.setDisplayName(ChatColor.GREEN+achievement.getDisplayName());
                        item.setItemMeta(itemMeta);
                        inv.setItem(inSlot,item);
                        inSlot++;
                    }
                } catch (AchievementFailedToLoadException e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"Failed to load: "+file.getName());
                    e.printStackTrace();
                }
            }
        }
        player.openInventory(inv);
    }
}
