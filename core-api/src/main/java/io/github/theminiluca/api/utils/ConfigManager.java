package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.LucaAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ConfigManager {

    private JavaPlugin plugin;
    private Map<String, Object> values = new HashMap<>();

    public void setup() {
        File def = new File(plugin.getDataFolder().toString());
        if (!def.exists()) def.mkdir();
        File config = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration configuration = null;
        if (config.exists()) {
            plugin.reloadConfig();
            plugin.saveConfig();
            for (Option option : Option.values()) {
                values.put(option.getPath(), plugin.getConfig().get(option.path, option.clazz));
            }
            configuration = plugin.getConfig();
            config.delete();
        }
        try {
            Files.copy(Objects.requireNonNull(plugin.getResource("config.yml")), config.toPath());
//            plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
//            try {
//                Files.copy(new File(plugin.getDataFolder() +
//                        "config.yml").toPath(), new FileOutputStream(config));
//                plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        }
        if (configuration != null) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(config);
            for (Option option : Option.values()) {
                option.object = plugin.getConfig().getObject(option.path, option.clazz);
                Object value = values.get(option.getPath());
                if (value == null) continue;
                yaml.set(option.getPath(), value);
            }
            yaml.set("version", plugin.getDescription().getVersion());
            try {
                yaml.save(config);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getDataFolder().mkdir();
        new File(plugin.getDataFolder() + File.separator + "data").mkdir();
        new File(LucaAPI.getInstance().getDataFolder() + "\\" + "storage").mkdir();
        instance = this;
    }

    public static ConfigManager instance;

    public String getString(Option e) {
        return plugin.getConfig().getString(e.path);
    }

    public boolean getBoolean(Option e) {
        return plugin.getConfig().getBoolean(e.path);
    }

    public double getDouble(Option e) {
        return plugin.getConfig().getDouble(e.path);
    }

    public int getInt(Option e) {
        return plugin.getConfig().getInt(e.path);
    }

    public List<String> getList(Option e) {
        return plugin.getConfig().getStringList(e.path);
    }

    public static class Option {

        public static final Set<Option> options = new HashSet<>();
        private final String path;
        private final Class<?> clazz;
        private Object object;

        public static Set<Option> values() {
            return options;
        }

        public Option(String path, Class<?> clazz) {
            this.path = path;
            this.clazz = clazz;
            options.add(this);
        }

        @SuppressWarnings("unchecked")
        public static <T> T getOption(Option option) {
            return (T) getOption(option, option.clazz);
        }
        @SuppressWarnings("unchecked")
        public static <T> T getOption(Option option, Class<T> clazz) {
            return (T) option.object;
        }

        public String getPath() {
            return path;
        }

        public Object getObject() {
            return object;
        }
    }
}