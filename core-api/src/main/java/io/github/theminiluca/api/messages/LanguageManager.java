package io.github.theminiluca.api.messages;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;

public class LanguageManager {

    private static final LinkedHashSet<String> SUPPORT_LANGUAGE = new LinkedHashSet<>();

    private static final Map<String, Map<String, String>> LANGUAGE_MESSAGES = new HashMap<>();

    public static String TRANSLATION_PATH = "messages/";

    public static void registerLanguages(String language) {
        SUPPORT_LANGUAGE.add(language);
    }


    public LinkedHashSet<String> getSupportLanguage() {
        return SUPPORT_LANGUAGE;
    }


    public static String getLanguage(String language, String propertiesKey) {
        return LANGUAGE_MESSAGES.get(language).get(propertiesKey);
    }


    public static String formatted(String message, Object... objects) {
        String s = message;
        if (objects == null) return s;
        if (objects.length == 0) return s;
        for (int i = 0; i < objects.length; i++) {
            s = s.replace("{" + i + "}", String.valueOf(objects[i]));
        }
        return s;
    }

    public Plugin plugin;

    public LanguageManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        File def = new File(plugin.getDataFolder().toString());
        if (!def.exists()) def.mkdir();
        Properties properties = new Properties();
        for (String lang : SUPPORT_LANGUAGE) {
            try {
                InputStream resource = plugin.getResource(TRANSLATION_PATH + lang + ".properties");
                assert resource != null;
                Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8);
                properties.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            LANGUAGE_MESSAGES.put(lang, new HashMap<>());
            properties.forEach((k, v) -> LANGUAGE_MESSAGES.get(lang).put(k.toString(), v.toString()));
        }
    }

    public static String getValueFromTag(String input) {
        int startIndex = input.indexOf(":");
        if (startIndex == -1) {
            return null;
        }
        int endIndex = input.indexOf(">");
        if (endIndex == -1) {
            return null;
        }
        return input.substring(startIndex + 1, endIndex);
    }

    public String getProperties(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) return "";
        return value;
    }
}
