package org.zycong.theHordes.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.ArrayList;
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

        if (args[0].equalsIgnoreCase("create")){
            if (args.length == 2){
                yamlManager.getInstance().setOption("lobbies", args[1] + ".location", p.getLocation());
                yamlManager.getInstance().setOption("lobbies", args[1] + ".map.location", new Location(Bukkit.getWorld("world"), 0d, 100d, 0d));
                yamlManager.getInstance().setOption("lobbies", args[1] + ".players", List.of());
                yamlManager.getInstance().setOption("lobbies", args[1] + ".map.spawnLoc", List.of(new Location(Bukkit.getWorld("world"), 0d, 100d, 0d)));
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.lobbies.create").toString()));
                return true;
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
                return true;
            }
        }else if (args[0].equalsIgnoreCase("delete")){
            if (args.length == 2){
                if (yamlManager.getInstance().getOption("lobbies", args[1] + ".location") != null){
                    yamlManager.getInstance().setOption("lobbies", args[1] + ".location", null);
                    yamlManager.getInstance().setOption("lobbies", args[1] + ".map.location", null);
                    yamlManager.getInstance().setOption("lobbies", args[1] + ".map.spawnLoc", null);
                    yamlManager.getInstance().setOption("lobbies", args[1] + ".players", null);
                    yamlManager.getInstance().setOption("lobbies", args[1], null);
                    p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.lobbies.delete").toString()));
                    return true;
                }
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("setMap")){
            if (args.length == 2){
                if (yamlManager.getInstance().getOption("lobbies", args[1] + ".location") != null) {
                    yamlManager.getInstance().setOption("lobbies", args[1] + ".map.location", p.getLocation());
                }
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("spawnPoint")){
            if (args.length == 3){
                if (args[2].equalsIgnoreCase("add")){
                    List<Location> spawns = new ArrayList<>();
                    spawns.addAll((List<Location>) yamlManager.getInstance().getOption("lobbies", args[1] + ".map.spawnLoc"));
                    spawns.add(p.getLocation());
                    yamlManager.getInstance().setOption("lobbies", args[1] + ".map.spawnLoc", spawns);
                    p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.lobbies.location.added").toString()));
                }
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
            return List.of("create", "delete", "setMap", "spawnPoint");
        } else if (args.length == 2){
            if (args[0].equals("delete") || args[0].equals("setMap") || args[0].equals("spawnPoint")){
                List<String> lobbiesss = new ArrayList<>();
                for (Object o : yamlManager.getInstance().getNodes("lobbies", "")){
                    if (o instanceof String string){lobbiesss.add(string); }
                }
                return lobbiesss;
            }
        } else if (args.length == 3){
            if (args[0].equalsIgnoreCase("spawnPoint")){
                return List.of("add");
            }
        }
        return List.of();
    }
}
