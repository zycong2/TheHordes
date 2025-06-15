package org.zycong.theHordes.commands.CommandRegister;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandWrapper extends Command {
    private final Object instance;
    private final Method method;
    private final CommandRegister.Command annotation;

    public CommandWrapper(String name, CommandRegister.Command annotation, Object instance, Method method) {
        super(name);
        this.instance = instance;
        this.method = method;
        this.annotation = annotation;
        setDescription(annotation.description());
        setPermission(annotation.permission());
        if (annotation.aliases().length > 0) {
            setAliases(Arrays.asList(annotation.aliases()));
        }
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        try {
            if(annotation.playerOnly() == true) {

                if(sender instanceof Player p) {method.invoke(instance, sender, args);}
                else{sender.sendMessage("Player ONLY!");}

            }else{method.invoke(instance, sender, args);}
        } catch (Exception e) {
            sender.sendMessage("§cCommand error: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        CommandRegister.Arguments[] suggestions = annotation.args();
        int index = args.length - 1;
        if (index < 0 || index >= suggestions.length) return List.of();
        List<CommandRegister.Arg> arguments = Arrays.asList(suggestions[index].args());
        return arguments.stream()
                .filter(arg -> sender.hasPermission(arg.permission()))
                .map(CommandRegister.Arg::arg)
                .collect(Collectors.toList());
    }
}
