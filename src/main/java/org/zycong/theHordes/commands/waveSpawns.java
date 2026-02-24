package org.zycong.theHordes.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.List;

import static org.zycong.theHordes.TheHordes.Colorize;

public class waveSpawns implements CommandHandler {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            return true;
        }
        if (!p.hasPermission("TheHordes.commands.entity")){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noPermission").toString()));
            return true;
        }

        switch (args[0]){
            case "entity" -> {
                Inventory inv = Bukkit.createInventory(p, 27, "Entities");
                int count = 0;
                for (EntityType ent : TheHordes.entities){
                    inv.addItem(new ItemStack(Material.valueOf(TheHordes.entities.get(count).name() + "_SPAWN_EGG")));
                    count++;
                }
                p.openInventory(inv);
                p.setMetadata("inventory", new FixedMetadataValue(TheHordes.getPlugin(), "entityEditor"));
            }
            case "boss" -> {
                if (args[1].equals("clear")){
                    TheHordes.bosses.clear();
                }
                if (args[1].equals("get")){
                    p.sendMessage(TheHordes.bosses.toString());
                }
                if (args[1].equals("add")){
                   TheHordes.bosses.add(args[2]);
                } if (args[1].equals("remove")){
                    TheHordes.bosses.remove(args[2]);
                }

            }
            default -> {

            }
        }


        return true;
    }

    public static void onEntityClose(InventoryCloseEvent event){
        Player p = (Player) event.getPlayer();
        p.removeMetadata("inventory", TheHordes.getPlugin());

        TheHordes.entities.clear();
        for (ItemStack item : event.getInventory().getContents()){
            if (item == null){ continue; }
            TheHordes.entities.add(EntityType.valueOf(item.getType().name().replace("_SPAWN_EGG", "")));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length ==1){
            return List.of("boss", "entity");
        } else{
            if (args[0].equals("entity")){
                return List.of("clear", "add", "get", "remove");
            }
        }
        return List.of();
    }
}
