package org.zycong.theHordes.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.List;

public class spawn implements CommandHandler {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.teleport((Location) yamlManager.getInstance().getOption("config", "spawn.location"));
            p.sendMessage((TextComponent)yamlManager.getInstance().getOption("messages", "command.success.spawn.tp"));
        } else {
            if (args[0].equals("set")){
                if (p.hasPermission("TheHordes.commands.setSpawn")){
                    Location loc = p.getLocation();
                    yamlManager.getInstance().setOption("config", "spawn.location", loc);
                    p.sendMessage((TextComponent)yamlManager.getInstance().getOption("messages", "command.success.spawn.set"));
                } else{
                    p.sendMessage((TextComponent)yamlManager.getInstance().getOption("messages", "command.failed.noPermission"));
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return List.of();
    }
}
