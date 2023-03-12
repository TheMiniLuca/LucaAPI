package io.github.theminiluca;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitListener implements Listener {

    BukkitListener(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
