package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.LucaAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static java.util.Objects.*;

public class ConfigManager {

    private JavaPlugin plugin;
    public final Map<String, Object> values = new HashMap<>();

    private YamlConfiguration config;

    public static void copy(@NotNull InputStream stream, @NotNull File file) {
        if (file.exists()) return;
        try {
            Files.copy(stream, file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(@NotNull File file, YamlConfiguration yaml) {
        try {
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    }


    public void setup() {
        File def = new File(plugin.getDataFolder().toString());
        def.mkdir();
        File config = new File(plugin.getDataFolder(), "config.yml");
        File old = new File(plugin.getDataFolder(), "config.yml_old");
        old.delete();
        copy(requireNonNull(plugin.getResource("config.yml")), old);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(config);
        YamlConfiguration yamlOld = YamlConfiguration.loadConfiguration(old);

        save(old, yamlOld);
        for (Option option : Option.values()) {
            if (yaml.isSet(option.path) && !yaml.get(option.path, option.clazz).equals(option.clazz)) {
                values.put(option.getPath(), (yaml.get(option.path, option.clazz)));
            } else {
                values.put(option.getPath(), (yamlOld.get(option.path, option.clazz)));
            }
        }
        if (config.exists())
            config.delete();
        copy(requireNonNull(plugin.getResource("config.yml")), config);
        yaml = YamlConfiguration.loadConfiguration(config);
        save(config, yaml);
        for (Option option : Option.values()) {
            Object value = values.getOrDefault(option.getPath(), null);
            System.out.println(value);
            if (value != null)
                yaml.set(option.getPath(), value);
            else {
                if (yamlOld.isSet(option.getPath()))
                    yaml.set(option.getPath(), yamlOld.get(option.getPath()));
            }
        }
        yaml.set("version", plugin.getDescription().getVersion());
        save(config, yaml);
        reloadConfig();
    }

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getDataFolder().mkdir();
        instance = this;
    }

    public static ConfigManager instance;

    public static ConfigManager getInstance() {
        return instance;
    }

    public String getString(Option e) {
        return getConfig().getString(e.path);
    }

    public boolean getBoolean(Option e) {
        return getConfig().getBoolean(e.path);
    }

    public double getDouble(Option e) {
        return getConfig().getDouble(e.path);
    }

    public int getInt(Option e) {
        return getConfig().getInt(e.path);
    }

    public List<String> getList(Option e) {
        return getConfig().getStringList(e.path);
    }

    public static class Option {

        public static final Set<Option> options = new HashSet<>();
        private final String path;
        private final Class<?> clazz;

        public static Set<Option> values() {
            return options;
        }

        public Option(String path, Class<?> clazz) {
            this.path = path;
            this.clazz = clazz;
            options.add(this);
        }

        public String getPath() {
            return path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Option option = (Option) o;
            return Objects.equals(path, option.path) && Objects.equals(clazz, option.clazz);
        }

        @Override
        public String toString() {
            return "Option{" +
                    "path='" + path + '\'' +
                    ", clazz=" + clazz +
                    '}';
        }

        @Override
        public int hashCode() {
            return hash(path, clazz);
        }
    }
}