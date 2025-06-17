package org.zycong.theHordes.commands.CommandRegister;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.zycong.theHordes.TheHordes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommandRegister {

    private static CommandRegister INSTANCE;

    private final JavaPlugin plugin;
    private final CommandMap commandMap;

    public CommandRegister(JavaPlugin plugin) {
        INSTANCE = this;
        this.plugin = plugin;

        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            this.commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get command map", e);
        }
    }

    public static CommandRegister global() {
        return INSTANCE;
    }

    public void registerCommands(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class)) continue;

            Command annotation = method.getAnnotation(Command.class);
            CommandWrapper wrapper = new CommandWrapper(
                    annotation.name(), annotation, obj, method
            );
            commandMap.register(annotation.root(), wrapper);
        }
    }

    // Class Finder
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface AutoRegisterer {}


    /*
      Copy this to the method parameters

      CommandSender commandSender, String[] args

      Command annotation itself
    */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Command {
        String name();
        Arguments[] args() default  {};
        String[] aliases() default {};
        String permission() default "";
        String description() default "A Command";
        String root() default "TheHordes"; // No space allow
        boolean playerOnly() default false;
    }

    // for tab completion
    public @interface Arguments {
        Arg[] args() default {};
    }

    public @interface Arg {
        String arg();
        String permission() default "";
    }
}