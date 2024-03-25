package io.github.theminiluca.api.messages;

import io.github.theminiluca.api.utils.Colour;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class LanguageManager {

    private static final LinkedHashSet<String> SUPPORT_LANGUAGE = new LinkedHashSet<>();

    public static final Map<String, Map<String, String>> LANGUAGE_MESSAGES = new HashMap<>();

    public static String TRANSLATION_RESOURCE_PATH = "messages/";
    public static String TRANSLATION_PLUGIN_TARGET_PATH = "translations/";

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
        File languageFile = new File(plugin.getDataFolder() + "\\\\" + TRANSLATION_PLUGIN_TARGET_PATH);
        if (!languageFile.exists()) {
            languageFile.mkdir();
        }
        for (String lang : SUPPORT_LANGUAGE) {
            Properties properties = new Properties();
            try {
                String path = plugin.getDataFolder() + File.separator + TRANSLATION_PLUGIN_TARGET_PATH + lang + ".properties";
                InputStream resource;
                if (!new File(path).exists()) {
                    resource = plugin.getResource(TRANSLATION_RESOURCE_PATH);
                } else {
                    resource = Files.newInputStream(Path.of(path)
                            , StandardOpenOption.READ);
                }
                if (resource == null) {
                    try (OutputStream output = new FileOutputStream(plugin.getDataFolder() + File.separator + TRANSLATION_PLUGIN_TARGET_PATH + lang + ".properties")) {
                        Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
                        properties.store(writer, lang);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8);
                    properties.load(reader);
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            try (OutputStream output = new FileOutputStream(languageFile.getPath() + "\\\\" + lang + ".properties")) {
                Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
                properties.store(writer, lang + " RoinPvP language file");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!LANGUAGE_MESSAGES.containsKey(lang))
                LANGUAGE_MESSAGES.put(lang, new HashMap<>());

            properties.forEach((k, v) -> {
                System.out.println(k.toString() + " : " + v.toString());
                LANGUAGE_MESSAGES.get(lang).put(k.toString(), v.toString());
            });
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

    public void save(Plugin plugin) {
        for (String lang : SUPPORT_LANGUAGE) {
            Properties properties = new Properties();
            for (Map.Entry<String, String> entry : LANGUAGE_MESSAGES.get(lang).entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }

            try (OutputStream output = new FileOutputStream(plugin.getDataFolder() + File.separator + TRANSLATION_PLUGIN_TARGET_PATH + lang + ".properties")) {
                Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
                properties.store(writer, lang + " language file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
