package com.gihub.theminiluca.api;

import com.gihub.theminiluca.api.command.CommandManager;
import com.gihub.theminiluca.api.event.impl.FakeSignUpdateEvent;
import com.gihub.theminiluca.api.user.IUser;
import com.gihub.theminiluca.api.utils.ConfigManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.gihub.theminiluca.api.utils.NMS;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class LucaAPI {

    public static JavaPlugin instance;

    public static String version;
    public static NMS nmsHandler;


    public static HashMap<String, IUser> users = new HashMap<>();

    public static IUser getUser(UUID uniqueId) {
        return users.getOrDefault(uniqueId.toString(), null);
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static IUser multiversion(UUID uniqueId, String name, long firstJoin)  {
        final Class<?> clazz;
        try {
            clazz = Class.forName("com.gihub.theminiluca.api." + version + ".user.CraftUser");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Constructor<?> constructor = clazz.getConstructor(UUID.class, String.class, long.class);
            return (IUser) constructor.newInstance(uniqueId, name, firstJoin);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void onEnable(JavaPlugin plugin) {
        instance = plugin;
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        LucaAPI.version = packageName.substring(packageName.lastIndexOf('.') + 1);
        final Class<?> clazz;
        try {
            clazz = Class.forName("com.github.theminiluca.api." + version + ".messages.NMSHandler");
            if (NMS.class.isAssignableFrom(clazz)) {
                    nmsHandler = (NMS) clazz.getConstructor().newInstance();
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        CommandManager.loadCommands();
        new BukkitListener(plugin);
        new ConfigManager(plugin).setup();
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter
                (getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                IUser user = LucaAPI.getUser(event.getPlayer().getUniqueId());
                Bukkit.getScheduler().runTask(instance, () -> {
                    FakeSignUpdateEvent signUpdateEvent = new FakeSignUpdateEvent(user, packet.getStringArrays().getValues().get(0));
                    Bukkit.getServer().getPluginManager().callEvent(signUpdateEvent);
                });
            }
        });
    }

}
