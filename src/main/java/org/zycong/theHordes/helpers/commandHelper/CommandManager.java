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
import org.zycong.theHordes.commands.*;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import static java.lang.Class.forName;

public class CommandManager implements CommandExecutor, TabCompleter {
    List<CommandExecutor> commandExecutors = List.of();
    List<String> TabCompletors = List.of();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.sendMessage((BaseComponent) yamlManager.getInstance().getOption("messages", "command.failed.noArgs"));
        } if (p.hasPermission("TheHordes.Command")){
            p.sendMessage((BaseComponent) yamlManager.getInstance().getOption("messages", "command.failed.noPermission"));
        }
        Reflections reflections = new Reflections("org.zycong.theHordes.commands"); // replace with your actual package

        Set<Class<? extends CommandHandler>> classes = reflections.getSubTypesOf(CommandHandler.class);

        for (Class<? extends CommandHandler> clazz : classes) {
            try {
                CommandHandler instance = clazz.getDeclaredConstructor().newInstance();

                instance.onCommand();

            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return List.of();
    }
}

