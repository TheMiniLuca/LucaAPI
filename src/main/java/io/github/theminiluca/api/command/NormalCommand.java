package io.github.theminiluca.command;

import io.github.theminiluca.roin.war.plugin.user.User;

public abstract class NormalCommand implements CommandI {
    public abstract void perform(User user, String[] args);
}
