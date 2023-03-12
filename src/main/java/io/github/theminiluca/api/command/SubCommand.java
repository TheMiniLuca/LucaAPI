package io.github.theminiluca.command;

import io.github.theminiluca.roin.war.plugin.user.User;
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

    public abstract void perform(User user, String[] args);
}
