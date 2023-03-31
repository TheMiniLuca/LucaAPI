package io.github.theminiluca.api;

import io.github.theminiluca.api.command.CommandManager;
import io.github.theminiluca.api.utils.ConfigManager;
import io.github.theminiluca.api.utils.NMS;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class LucaAPI {

    public static JavaPlugin instance;

    public static String version;
    public static NMS nmsHandler;



    public static JavaPlugin getInstance() {
        return instance;
    }

//    public static IUser multiversion(UUID uniqueId, String name, long firstJoin)  {
//        final Class<?> clazz;
//        try {
//            clazz = Class.forName("io.github.theminiluca.api." + version + ".user.CraftUser");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            Constructor<?> constructor = clazz.getConstructor(UUID.class, String.class, long.class);
//            return (IUser) constructor.newInstance(uniqueId, name, firstJoin);
//        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void onEnable(JavaPlugin plugin) {
        LucaAPI.instance = plugin;
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        LucaAPI.version = packageName.substring(packageName.lastIndexOf('.') + 1);
        final Class<?> clazz;
        try {
            clazz = Class.forName("io.github.theminiluca.api." + version + ".nms.NMSHandler");
            if (NMS.class.isAssignableFrom(clazz)) {
                nmsHandler = (NMS) clazz.getConstructor().newInstance();
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        CommandManager.loadCommands();
//        new BukkitListener(getInstance());
        new ConfigManager(getInstance()).setup();
    }

}
