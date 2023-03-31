package io.github.theminiluca.api;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class BukkitListener implements Listener {

//    BukkitListener(JavaPlugin plugin) {
//        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
//    }
//
//    @EventHandler
//    public void onJoin(PlayerJoinEvent event) {
//        IUser user = LucaAPI.getUser(event.getPlayer().getUniqueId());
//        UUID uniqueId = event.getPlayer().getUniqueId();
//        if (user == null)
//            LucaAPI.users.put(uniqueId.toString(),
//                    LucaAPI.multiversion(uniqueId, event.getPlayer().getName(), System.currentTimeMillis()));
//    }
}
