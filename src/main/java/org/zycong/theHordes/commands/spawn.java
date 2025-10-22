package org.zycong.theHordes.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zycong.theHordes.commands.CommandRegister.CommandRegister;
import org.zycong.theHordes.helpers.Lobby.lobbyManager;
import org.zycong.theHordes.helpers.yaml.yamlManager;
import org.zycong.theHordes.TheHordes;

import static org.zycong.theHordes.TheHordes.Colorize;
import static org.zycong.theHordes.helpers.PDCHelper.setItemPDC;

@CommandRegister.AutoRegisterer
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
            p.teleport(TheHordes.stringToLocation((String) yamlManager.getInstance().getOption("config", "spawn.location")));
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.spawn.tp").toString()));
            lobbyManager.playerAwayFromGame(p);
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();

            ItemStack startGame = new ItemStack(Material.LIME_CONCRETE, 1);
            ItemMeta meta = startGame.getItemMeta();
            meta.displayName(Colorize("&aStart game!"));
            startGame.setItemMeta(meta);
            setItemPDC("events", startGame, "startGame");
            p.getInventory().setItem(4, startGame);
            return true;
        } else {
            if (args[0].equals("set")){
                if (p.hasPermission("TheHordes.commands.setSpawn")){
                    Location loc = p.getLocation();
                    yamlManager.getInstance().setOption("config", "spawn.location", TheHordes.locationToString(loc));
                    p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.success.spawn.set").toString()));
                } else{
                    p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noPermission").toString()));
                }
            }
            return true;
        }
    }
}
