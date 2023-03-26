package com.gihub.theminiluca.api.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Colour {

    private static final Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
    private static final Pattern defaults = Pattern.compile("&[0-9a-fA-F]");

    public static final String BLACK = ChatColor.BLACK.toString();
    public static final String DARK_BLUE = ChatColor.DARK_BLUE.toString();
    public static final String DARK_GREEN = ChatColor.DARK_GREEN.toString();
    public static final String DARK_AQUA = ChatColor.DARK_AQUA.toString();
    public static final String DARK_RED = ChatColor.DARK_RED.toString();
    public static final String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
    public static final String GOLD = ChatColor.GOLD.toString();
    public static final String GRAY = ChatColor.GRAY.toString();
    public static final String DARK_GRAY = ChatColor.DARK_GRAY.toString();
    public static final String BLUE = ChatColor.BLUE.toString();
    public static final String GREEN = ChatColor.GREEN.toString();
    public static final String AQUA = ChatColor.AQUA.toString();
    public static final String RED = ChatColor.RED.toString();
    public static final String LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();
    public static final String YELLOW = ChatColor.YELLOW.toString();
    public static final String WHITE = ChatColor.WHITE.toString();

    public static final Special BOLD = new Special(ChatColor.BOLD);
    public static final Special STRIKETHROUGH = new Special(ChatColor.STRIKETHROUGH);
    public static final Special UNDERLINE = new Special(ChatColor.UNDERLINE);
    public static final Special ITALIC = new Special(ChatColor.ITALIC);
    public static final Special MAGIC = new Special(ChatColor.MAGIC);
    public static final String RESET = ChatColor.RESET.toString();

    public static final String EXPLAIN = Colour.ITALIC.append(Colour.GRAY);
    public static final String EXTRACT_COLOR = Colour.ITALIC.append(Colour.DARK_GRAY);

    public static String colour(String decode) {
        return ChatColor.of(Color.decode("#%s".formatted(decode))).toString();
    }

    public static String format(String msg) {
        Matcher patternC = pattern.matcher(msg);
        while (patternC.find()) {
            String color = msg.substring(patternC.start(), patternC.end());
            msg = msg.replace(color, ChatColor.of(color.replace("<", "").replace(">", "")).toString());
            patternC = pattern.matcher(msg);
        }
        Matcher defaultsC = defaults.matcher(msg);
        while (defaultsC.find()) {
            String color = msg.substring(defaultsC.start(), defaultsC.end());
            msg = msg.replace(color, ChatColor.getByChar(color.replace("&", "").charAt(0)).toString());
            defaultsC = defaults.matcher(msg);
        }
        return msg;
    }

    public static String formatted(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String colour(int r, int g, int b) {
        return ChatColor.of(new Color(r, g, b)).toString();
    }

    public static String colour() {
        Random random = new Random();
        return colour(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public final static class Special {

        private final ChatColor color;

        private Special(ChatColor color) {
            this.color = color;
        }

        public String append(String colour) {
            return colour + color;
        }

        @Override
        public String toString() {
            return color.toString();
        }
    }


}
