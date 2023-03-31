package io.github.theminiluca.api.event.impl;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FakeSignUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final String[] line;

    public FakeSignUpdateEvent(String[] line) {
        this.line = line;
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