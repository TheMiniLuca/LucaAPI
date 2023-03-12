package io.github.theminiluca.command;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandExecutor implements CommandI {


    public abstract String label();

    public abstract List<SubCommand> commandList();

    public String description(SubCommand command) {
        if (command.description() == null)
            return label() + "." + command.name() + ".desc";
        else return command.description();
    }

    public String name(SubCommand command) {
        if (translatable())
            return label() + "." + command.name() + ".command";
        else
            return command.name();
    }

    public boolean translatable() {
        return true;
    }



    public String[] syntax(SubCommand command) {
        List<String> syntax = new ArrayList<>();
        if (!translatable())
            syntax.add(label());
        else
            syntax.add(label() + ".command");
        syntax.add(" ");
        syntax.add(name(command));
        for (int i = 0; i < command.syntax().length; i++) {
            syntax.add(" ");
            syntax.add(command.syntax()[i]);
        }
        return syntax.toArray(new String[0]);
    }


}
