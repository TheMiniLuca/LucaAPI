package io.github.theminiluca.api;

import io.github.theminiluca.api.command.CommandManager;
import io.github.theminiluca.api.event.ArmorListener;
import io.github.theminiluca.api.event.DispenserArmorListener;
import io.github.theminiluca.api.utils.ConfigManager;
import io.github.theminiluca.api.utils.NMS;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class LucaAPI {

    public static JavaPlugin instance;

    public static String version;
    public static NMS nmsHandler;



    public static JavaPlugin getInstance() {
        return instance;
    }

    public static ItemStack localizedItem(String unique, ItemStack item) {
        ItemStack itemStack = item.clone();
        if (itemStack.getType().isAir()) throw new IllegalArgumentException("아이템은 AIR 타입이 될 수 없습니다.");
        ItemMeta im = itemStack.getItemMeta();
        assert im != null;
        im.setLocalizedName(unique);
        itemStack.setItemMeta(im);
        return itemStack;
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

    public static String localized(ItemStack is) {
        return is == null || is.getType().isAir() || is.getItemMeta() == null ? null : is.getItemMeta().getLocalizedName();
    }

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
        CommandManager.loadCommands(plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ArmorListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new DispenserArmorListener(), instance);
//        new BukkitListener(getInstance());
        new ConfigManager(getInstance()).setup();
    }

}
