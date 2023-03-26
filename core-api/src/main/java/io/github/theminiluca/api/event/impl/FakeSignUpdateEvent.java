package io.github.theminiluca.api.event.impl;

import io.github.theminiluca.api.user.IUser;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FakeSignUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final IUser user;
    private final String[] line;

    public FakeSignUpdateEvent(@NotNull IUser user, String[] line) {
        this.user = user;
        this.line = line;
    }

    public IUser getUser() {
        return user;
    }

    public String[] getLine() {
        return line;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}