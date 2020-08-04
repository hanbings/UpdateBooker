package com.nanokylin.mc.updatebooker.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 好像没啥写的 一个类完成吧
 *
 * @author Hanbings
 */
public class PlayerJoin implements Listener {
    FileConfiguration message = YamlConfiguration.loadConfiguration(new File("plugins/UpdateBooker/message.yml"));

    /**
     * 就在这里完成它！
     *
     * @param event authme给的登录后的触发的事件
     * @author Hanbings
     */
    @EventHandler
    public void LoginEvent(LoginEvent event) {
        // 检索版本号
        File data = new File("plugins/UpdateBooker/data/" + message.getString("version") + ".yml");
        if (!data.exists()) {
            this.createFile("plugins/UpdateBooker/data/" + message.getString("version") + ".yml");
        }
        Player p = event.getPlayer();
        FileConfiguration temp = YamlConfiguration.loadConfiguration(data);
        if (!temp.getBoolean(p.getName())) {

            temp.set(p.getName(), true);
            try {
                temp.save(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            openBook(newBook("HelloWorld", "UpdateBooker", ChatColor.translateAlternateColorCodes('&', message.getString("message").replace("\\n", "\n"))), p);
        }
    }

    /**
     * 创建一个新文件
     */
    public void createFile(String path) {
        File file = new File("plugins/UpdateBooker/data");
        //如果文件夹不存在
        if (!file.exists()) {
            //创建文件夹
            Boolean mkdirs = file.mkdirs();
        }
        //异常处理
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.close();
            //一定要关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lazy 太长了 仅仅是把创建书独立出来
     *
     * @param title  标题
     * @param author 作者
     * @param pages  内容
     * @return 当然是返回书本啦
     * @author Hanbings
     */
    public ItemStack newBook(String title, String author, String... pages) {
        BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        meta.setTitle(title);
        meta.setAuthor(author);
        meta.setPages(pages);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        return book;
    }

    /**
     * 使用ProtocolLib发包
     *
     * @param book 书本啦
     * @param p    玩家啦
     * @author Hanbings
     */
    public void openBook(ItemStack book, Player p) {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);
        PacketContainer pc = pm.createPacket(PacketType.Play.Server.OPEN_BOOK);
        try {
            pm.sendServerPacket(p, pc);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send open book packet " + pc, e);
        }
        p.getInventory().setItem(slot, old);
    }
}
