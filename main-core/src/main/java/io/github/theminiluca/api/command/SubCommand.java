package io.github.theminiluca.api.command;

import io.github.theminiluca.api.user.IUser;
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

    public abstract void perform(IUser user, String[] args);
}
