package io.github.theminiluca.api;

import io.github.theminiluca.api.command.CommandManager;
import io.github.theminiluca.api.user.IUser;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class LucaAPI {

    public static JavaPlugin instance;


    public static HashMap<String, IUser> users = new HashMap<>();

    public static IUser getUser(UUID uniqueId) {
        return users.getOrDefault(uniqueId.toString(), null);
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public void onEnable(JavaPlugin plugin) {
        instance = plugin;
        CommandManager.loadCommands();
        new BukkitListener(plugin);
    }

}
