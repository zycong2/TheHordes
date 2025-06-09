package org.zycong.theHordes.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.List;

import static org.zycong.theHordes.TheHordes.Colorize;

public class spawn implements CommandHandler {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.teleport((Location) yamlManager.getInstance().getOption("config", "spawn.location"));
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.spawn.tp").toString()));
        } else {
            if (args[0].equals("set")){
                if (p.hasPermission("TheHordes.commands.setSpawn")){
                    Location loc = p.getLocation();
                    yamlManager.getInstance().setOption("config", "spawn.location", loc);
                    p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.spawn.set").toString()));
                } else{
                    p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noPermission").toString()));
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("TheHordes.commands.setSpawn")){
            return List.of("set");
        }
        return List.of();
    }
}
