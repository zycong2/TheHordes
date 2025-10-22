package org.zycong.theHordes.helpers.commandHelper;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.reflections.Reflections;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.zycong.theHordes.TheHordes.Colorize;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();
    private final List<String> commandNames = new ArrayList<>();
    private boolean initialized = false;

    private void initializeHandlers() {
        if (initialized) return;

        Reflections reflections = new Reflections("org.zycong.theHordes.commands");
        Set<Class<? extends CommandHandler>> classes = reflections.getSubTypesOf(CommandHandler.class);

        for (Class<? extends CommandHandler> clazz : classes) {
            try {
                CommandHandler instance = clazz.getDeclaredConstructor().newInstance();
                String commandName = makeCommandName(clazz.getName());

                commandHandlers.put(commandName, instance);
                commandNames.add(commandName);

            } catch (InstantiationException | NoSuchMethodException |
                     InvocationTargetException | IllegalAccessException e) {
                // Log error but don't crash - use logger if available, otherwise print to console
                System.err.println("Failed to initialize command handler for " + clazz.getName() + ": " + e.getMessage());
            }
        }
        initialized = true;
    }

    // Call this method from your plugin's onEnable() to pre-initialize
    public void initialize() {
        initializeHandlers();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player p = (Player) commandSender;

        // Lazy initialization if not already done
        if (!initialized) {
            initializeHandlers();
        }

        if (args.length == 0) {
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            return true;
        }

        if (!p.hasPermission("TheHordes.Command")) {
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noPermission").toString()));
            return true;
        }

        String commandToExecute = args[0];
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        CommandHandler handler = commandHandlers.get(commandToExecute);
        if (handler != null) {
            handler.onCommand(commandSender, command, s, newArgs);
        } else {
            // Command not found - you might want to send an error message here
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.notFound").toString()));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Lazy initialization if not already done
        if (!initialized) {
            initializeHandlers();
        }

        if (args.length == 1) {
            // Filter command names based on what the user has typed so far
            String input = args[0].toLowerCase();
            if (input.isEmpty()) {
                return new ArrayList<>(commandNames);
            }

            List<String> completions = new ArrayList<>();
            for (String cmd : commandNames) {
                if (cmd.toLowerCase().startsWith(input)) {
                    completions.add(cmd);
                }
            }
            return completions;
        }

        // For sub-command tab completion
        if (args.length >= 2) {
            String commandName = args[0];
            CommandHandler handler = commandHandlers.get(commandName);

            if (handler != null) {
                String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                List<String> result = handler.onTabComplete(commandSender, command, s, newArgs);
                return result != null ? result : List.of();
            }
        }

        return List.of();
    }

    private String makeCommandName(String input) {
        return input.replace("org.zycong.theHordes.commands.", "");
    }

    // Method to reload command handlers if needed (for plugin reloads)
    public void reload() {
        commandHandlers.clear();
        commandNames.clear();
        initialized = false;
        initializeHandlers();
    }

    // Utility method to get all registered command names (optional)
    public List<String> getCommandNames() {
        if (!initialized) {
            initializeHandlers();
        }
        return new ArrayList<>(commandNames);
    }
}