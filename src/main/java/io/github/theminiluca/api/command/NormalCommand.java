package io.github.theminiluca.api.command;


import io.github.theminiluca.api.user.IUser;

public abstract class NormalCommand implements CommandI {
    public abstract void perform(IUser user, String[] args);
}
