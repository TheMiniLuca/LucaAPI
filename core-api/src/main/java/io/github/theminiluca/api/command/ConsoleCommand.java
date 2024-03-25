package io.github.theminiluca.api.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class ConsoleCommand implements CommandI {
    public abstract void perform(CommandSender sender, String[] args);

    public abstract List<String> tabcomplete(CommandSender sender, String[] args);
}
