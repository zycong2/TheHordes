package org.zycong.theHordes.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zycong.theHordes.commands.CommandRegister.CommandRegister;
import org.zycong.theHordes.helpers.Lobby.lobbyManager;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import static org.zycong.theHordes.TheHordes.Colorize;

public class spawn {

    @CommandRegister.Command(
            name = "spawn",
            playerOnly = true,
            permission = "theHordes.spawn",
            aliases = "",
            args =
                    @CommandRegister.Arguments(args = {
                            @CommandRegister.Arg(arg = "set", permission = "TheHordes.commands.setSpawn")
                    }),
            description = "Teleport you to spawn!"
    )
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.teleport((Location) yamlManager.getInstance().getOption("config", "spawn.location"));
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.spawn.tp").toString()));
            lobbyManager.playerAwayFromGame(p);
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
}
