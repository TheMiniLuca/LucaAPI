package io.github.theminiluca.api.command;


import io.github.theminiluca.api.user.IUser;

import java.util.List;

public abstract class NormalCommand implements CommandI {
    public abstract void perform(IUser user, String[] args);
    public abstract List<String> tabcomplete(IUser user, String[] args);
}
