package org.zycong.theHordes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.List;

import static org.zycong.theHordes.TheHordes.Colorize;

public class lobbies implements CommandHandler {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            return true;
        }
        if (!p.hasPermission("TheHordes.commands.lobbies")){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noPermission").toString()));
            return true;
        }

        if (args[0].equals("create")){
            if (args.length == 2){
                yamlManager.getInstance().setOption("lobbies", args[1] + ".location", p.getLocation());
                yamlManager.getInstance().setOption("lobbies", args[1] + ".players", List.of());
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.lobbies.create").toString()));
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
                return true;
            }
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1){
            return List.of("create", "delete");
        } else if (args.length == 2){
            if (args[0].equals("delete")){
                List<String> lobbiesss = List.of();
                for (Object o : yamlManager.getInstance().getNodes("lobbies", "")){
                    if (o instanceof String string){lobbiesss.add(string); }
                }
                return lobbiesss;
            }
        }
        return List.of();
    }
}
