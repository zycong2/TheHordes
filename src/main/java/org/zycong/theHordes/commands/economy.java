package org.zycong.theHordes.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.ArrayList;
import java.util.List;

import static org.zycong.theHordes.TheHordes.Colorize;
import static org.zycong.theHordes.helpers.economy.*;
/*
public class economy implements CommandHandler {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            return true;
        }
        if (!p.hasPermission("TheHordes.commands.economy")){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noPermission").toString()));
            return true;
        }

        if (args[0].equalsIgnoreCase("giveMoney")){
            if (args[2] != null) {
                giveMoney(Bukkit.getPlayer(args[1]), Double.parseDouble(args[2]));
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            }
        }
        if (args[0].equalsIgnoreCase("removeMoney")){
            if (args[2] != null) {

                subtractMoney(Bukkit.getPlayer(args[1]), Double.parseDouble(args[2]));
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            }
        }
        if (args[0].equalsIgnoreCase("clearMoney")){
            if (args[1] != null) {
                resetMoney(Bukkit.getPlayer(args[1]));
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            }
        }

        if (args[0].equalsIgnoreCase("getBalance")){
            if (args[1] != null) {
                p.sendMessage(Bukkit.getPlayer(args[1]) + " has " + getBalance(Bukkit.getPlayer(args[1]))+ "coins");
            }else{
                p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return List.of("giveMoney", "clearMoney", "removeMoney", "getBalance");
        } else if (args.length == 2){
            List<String> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()){
                players.add(p.getName());
            }
            return players;
        }

        return List.of();
    }
}
*/