package io.github.theminiluca.api.command;

import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.messages.ComponentText;
import io.github.theminiluca.api.user.IUser;
import io.github.theminiluca.api.utils.Colour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandManager implements org.bukkit.command.CommandExecutor, TabCompleter {

    public static final Map<String, CommandI> LABELS = new HashMap<>();

    public static void register(CommandI commandLabel) {
        LABELS.put(commandLabel.label(), commandLabel);
    }

    @SuppressWarnings("unchecked")
    public static void loadCommands() {
        CommandManager cmd = new CommandManager();
        PluginDescriptionFile plugin = LucaAPI.getInstance().getDescription();
        for (Map.Entry<String, Map<String, Object>> command : plugin.getCommands().entrySet()) {
            setCommand(command.getKey(), cmd);
            if (command.getValue().containsKey("aliases")) {
                for (String aliases : (List<String>) command.getValue().get("aliases")) {
                    setCommand(aliases, cmd);
                }
            }
        }
    }

    private static void setCommand(final String cmd, final org.bukkit.command.CommandExecutor executor) {
        PluginCommand command = LucaAPI.getInstance().getCommand(cmd);
        if (command != null) {
            command.setExecutor(executor);
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        String label = command.getLabel();
        if (!(commandSender instanceof Player player)) return false;
        IUser user = LucaAPI.getUser(player.getUniqueId());
        assert user != null;
        for (int i = 0; i < LABELS.size(); i++) {
            if (LABELS.containsKey(label)) {
                if (!(LABELS.get(label) instanceof CommandExecutor executor)) {
                    NormalCommand normalCommand = (NormalCommand) LABELS.get(label);
                    normalCommand.perform(user, args);
                    return false;
                }
                if (args.length == 0) {
                    for (int j = 0; j < executor.commandList().size(); j++) {
                        SubCommand sub = executor.commandList().get(j);
                        String explain = user.translatable(executor.description(sub));
                        String syntax = user.translatable(executor.syntax(sub));
                        user.sendText(ComponentText.text("/" + Colour.WHITE + syntax + Colour.EXPLAIN + " - " + explain));
                    }
                    return false;
                }
                for (int j = 0; j < executor.commandList().size(); j++) {
                    SubCommand sub = executor.commandList().get(j);
                    if (args[0].equalsIgnoreCase(user.translatable(executor.name(sub)))) {
                        String[] array = Arrays.copyOfRange(args, 1, args.length);
                        if (array.length < sub.syntax().length) {
                            user.sendText(ComponentText.text("/" + user.translatable(executor.syntax(sub))));
                            return false;
                        }
                        executor.commandList().get(j).perform(user, array);
                        return false;
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
        for (int i = 0; i < LABELS.size(); i++) {
            if (LABELS.containsKey(label)) {
                if (!(LABELS.get(label) instanceof CommandExecutor commandLabel)) {
                    return null;
                }
                if (!(commandSender instanceof Player player)) return new ArrayList<>();
                IUser user = LucaAPI.getUser(player.getUniqueId());
                assert user != null;
                for (int j = 0; j < commandLabel.commandList().size(); j++) {
                    SubCommand sub = commandLabel.commandList().get(j);
                    arguments.add(user.translatable(user.translatable(commandLabel.name(sub))));
                }
                for (String argument : arguments) {
                    if (argument.toLowerCase().startsWith(args[0].toLowerCase())) {
                        complete.add(argument);
                    }
                }
                return complete;
            }
        }
        return null;
    }
}
