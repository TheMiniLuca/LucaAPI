package io.github.theminiluca.api.messages;

import io.github.theminiluca.api.utils.Duration;
import org.jetbrains.annotations.Nullable;

public interface BaseUser {

    String translatable(String locale, String key);
    String translatable(String key);
    void startCoolTime(String key, Duration delay);
    boolean cooltime(@Nullable String waringMessage, String key, Duration delay);
    boolean isCooltime(String key);

    void setCooltime(String key, boolean cooltime);
}
