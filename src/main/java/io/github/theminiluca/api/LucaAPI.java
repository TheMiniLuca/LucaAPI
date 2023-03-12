package io.github.theminiluca;

import io.github.theminiluca.command.CommandManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class LucaAPI {

    public static JavaPlugin instance;


    public static JavaPlugin getInstance() {
        return instance;
    }

    public void onEnable(JavaPlugin plugin) {
        instance = plugin;
        CommandManager.loadCommands();
        new BukkitListener();
    }
}
