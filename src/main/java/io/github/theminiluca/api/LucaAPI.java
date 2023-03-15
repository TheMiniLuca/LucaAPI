package io.github.theminiluca.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.github.theminiluca.api.command.CommandManager;
import io.github.theminiluca.api.event.impl.FakeSignUpdateEvent;
import io.github.theminiluca.api.user.IUser;
import io.github.theminiluca.api.utils.ConfigManager;
import org.bukkit.Bukkit;
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
