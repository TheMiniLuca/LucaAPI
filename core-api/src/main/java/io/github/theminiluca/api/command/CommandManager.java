package io.github.theminiluca.api.command;


import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandManager implements CommandExecutor, TabCompleter {

    private static CommandManager instance;

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }


    public static List<String> toTabComplete(List<String> list, String[] args) {
        List<String> complete = new ArrayList<>();
        List<String> arguments = new ArrayList<>(list);

        for (String argument : arguments) {
            if (argument.toLowerCase().startsWith(args[0].toLowerCase())) {
                complete.add(argument);
            }
        }
        return complete;
    }

    public final Map<String, CommandI> LABELS = new HashMap<>();

    public static void register(CommandI commandLabel) {
        getInstance().LABELS.put(commandLabel.label(), commandLabel);
    }

    @SuppressWarnings("unchecked")
    public static void loadCommands(JavaPlugin javaplugin) {
        PluginDescriptionFile plugin = javaplugin.getDescription();
        for (Map.Entry<String, Map<String, Object>> command : plugin.getCommands().entrySet()) {
            setCommand(javaplugin, command.getKey(), getInstance());
            if (command.getValue().containsKey("aliases")) {
                for (String aliases : (List<String>) command.getValue().get("aliases")) {
                    setCommand(javaplugin, aliases, getInstance());
                }
            }
        }
    }

    private static void setCommand(JavaPlugin plugin, final String cmd, final org.bukkit.command.CommandExecutor executor) {
        PluginCommand command = plugin.getCommand(cmd);
        if (command != null) {
            command.setExecutor(executor);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        String label = command.getLabel();
        if (LABELS.containsKey(label)) {
            CommandI commandI = LABELS.get(label);
            if ((commandI instanceof ConsoleCommand consoleCommand)) {
                consoleCommand.perform(commandSender, args);
            }
            if ((commandSender instanceof Player player)) {
                if (commandI instanceof NormalCommand normalCommand) {
                    normalCommand.perform(player, s, args);
                    return false;
                } else if (commandI instanceof CommandLabel executor) {
                    if (args.length == 0) {
                        executor.commandHelp(player).run();
                        return false;
                    }
                    for (int j = 0; j < executor.commandList().size(); j++) {
                        SubCommand sub = executor.commandList().get(j);
                        if (args[0].equalsIgnoreCase(executor.name(sub))) {
                            String[] array = Arrays.copyOfRange(args, 1, args.length);
                            if (array.length < sub.syntax().length) {
                                StringBuilder sb = new StringBuilder();
                                for (String syntax : executor.syntax(sub)) {
                                    sb.append(syntax).append(" ");
                                }
                                player.sendMessage("/" + sb);
                                return false;
                            }
                            executor.commandList().get(j).perform(player, array);
                            return false;
                        }
                    }
                }
            }
        }

        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        String label = command.getLabel();
        List<String> complete = new ArrayList<>();
        List<String> arguments = new ArrayList<>();
        if (LABELS.containsKey(label)) {
            CommandI commandI = LABELS.get(label);
            if (commandI instanceof ConsoleCommand consoleCommand) {
                return consoleCommand.tabcomplete(commandSender, args);
            }
            if (commandSender instanceof Player player) {
                if (commandI instanceof NormalCommand normalCommand) {
                    return normalCommand.tabcomplete(player, s, args);
                }
                CommandLabel commandLabel = (CommandLabel) commandI;
                for (int j = 0; j < commandLabel.commandList().size(); j++) {
                    SubCommand sub = commandLabel.commandList().get(j);
                    arguments.add(commandLabel.name(sub));
                }
                for (String argument : arguments) {
                    if (argument.toLowerCase().startsWith(args[0].toLowerCase())) {
                        complete.add(argument);
                    }
                }
                return complete;

            }
        }
        return new ArrayList<>();
    }
}
