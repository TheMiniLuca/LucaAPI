package io.github.theminiluca.api.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class SubCommand {

    public @NotNull
    abstract String name();
    public String[] syntax() {
        return new String[0];
    }


    public String description() {
        return null;
    }

    public abstract void perform(Player user, String[] args);
}
