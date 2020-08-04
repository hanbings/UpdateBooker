package com.nanokylin.mc.updatebooker.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.sun.org.apache.xpath.internal.objects.XNull;
import fr.xephi.authme.events.LoginEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * 好像没啥写的 一个类完成吧
 * @author Hanbings
 */
public class PlayerJoin implements Listener{
    /**
     * 就在这里完成它！
     * @param event authme给的登录后的触发的事件
     * @author Hanbings
     */
    @EventHandler
    public void LoginEvent(LoginEvent event){
        Player p = event.getPlayer();
        FileConfiguration message = YamlConfiguration.loadConfiguration(new File("plugins/UpdateBooker/message.yml"));
        openBook(newBook("HelloWorld","UpdateBooker", ChatColor.translateAlternateColorCodes('&', message.getString("message").replace("\\n", "\n"))),p);

    }
    public ItemStack newBook(String title, String author, String... pages)
    {
        BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        meta.setTitle(title);
        meta.setAuthor(author);
        meta.setPages(pages);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        return book;
    }
    public void openBook(ItemStack book, Player p)
    {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);
        PacketContainer pc = pm.createPacket(PacketType.Play.Server.OPEN_BOOK);
        try
        {
            pm.sendServerPacket(p, pc);
        } catch (InvocationTargetException e)
        {
            throw new RuntimeException("Cannot send open book packet " + pc, e);
        }
        p.getInventory().setItem(slot, old);
    }
}
