package io.github.theminiluca.api.messages;

public interface BaseUser {

    String translatable(String locale, String key);
    String translatable(String key);
}
