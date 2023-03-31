package io.github.theminiluca.api.command;

import io.github.theminiluca.api.utils.Colour;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandLabel implements CommandI {


    public abstract String label();

    public abstract List<SubCommand> commandList();

    public BukkitRunnable commandHelp(Player player) {
        CommandLabel label = this;
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (int j = 0; j < label.commandList().size(); j++) {
                    SubCommand sub = label.commandList().get(j);
                    String explain = label.description(sub);
                    StringBuilder sb = new StringBuilder();
                    for (String syntax : label.syntax(sub)) {
                        sb.append(syntax).append(" ");
                    }
                    String syntax = sb.toString();
                    player.sendMessage("/" + Colour.WHITE + syntax + Colour.EXPLAIN + " - " + explain);
                }
            }
        };
    };

    public String description(SubCommand command) {
        return command.description();
    }

    public String name(SubCommand command) {
        return command.name();
    }


    public String[] syntax(SubCommand command) {
        List<String> syntax = new ArrayList<>();
        syntax.add(label());
        syntax.add(" ");
        syntax.add(name(command));
        for (int i = 0; i < command.syntax().length; i++) {
            syntax.add(" ");
            syntax.add(command.syntax()[i]);
        }
        return syntax.toArray(new String[0]);
    }


}
