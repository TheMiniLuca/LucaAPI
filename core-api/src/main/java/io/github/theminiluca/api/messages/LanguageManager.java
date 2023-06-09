package io.github.theminiluca.api.messages;

import io.github.theminiluca.api.utils.Colour;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LanguageManager {

    private static final LinkedHashSet<String> SUPPORT_LANGUAGE = new LinkedHashSet<>();

    public static final Map<String, Map<String, String>> LANGUAGE_MESSAGES = new HashMap<>();

    public static String TRANSLATION_PATH = "messages/";

    public static void registerLanguages(String language) {
        SUPPORT_LANGUAGE.add(language);
    }


    public static LinkedHashSet<String> getSupportLanguage() {
        return SUPPORT_LANGUAGE;
    }


    public static String getLanguage(String language, String propertiesKey) throws NullPointerException {
        return LANGUAGE_MESSAGES.get(language).get(propertiesKey);
    }

    public static String getLanguageColor(String language, String propertiesKey) throws NullPointerException {
        return Colour.format(getLanguage(language, propertiesKey));
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


    public LanguageManager() {

    }

    public void setup(Plugin plugin) {
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
            if (!LANGUAGE_MESSAGES.containsKey(lang))
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
