package com.nanokylin.mc.updatebooker;

import com.nanokylin.mc.updatebooker.event.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main Class
 *
 * @author Hnabings
 */
public final class UpdateBooker extends JavaPlugin {
    /**
     * The plugin on enable
     *
     * @author Hanbings
     */
    @Override
    public void onEnable() {
        // 监听事件
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        // 加载配置文件
        File message = new File("plugins/UpdateBooker/message.yml");
        if (message.exists()) {
            Bukkit.getServer().getLogger().info("[UpdateBooker] 配置文件已加载");
        } else {
            Bukkit.getServer().getLogger().info("[UpdateBooker] 未找到配置文件 正在创建配置文件");
            saveResource("message.yml", false);
        }
        // 废话
        Bukkit.getServer().getLogger().info("[UpdateBooker] 已加载");

    }

    /**
     * The plugin on disable
     *
     * @author Hanbings
     */
    @Override
    public void onDisable() {
        Bukkit.getServer().getLogger().info("[UpdateBooker] 已卸载");
    }
}
