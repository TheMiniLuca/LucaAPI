package io.github.theminiluca.api.command;


import org.bukkit.entity.Player;

import java.util.List;

public abstract class NormalCommand implements CommandI {
    public abstract void perform(Player sender, String command, String[] args);

    public abstract List<String> tabcomplete(Player sender, String command, String[] args);
}
