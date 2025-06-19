package org.zycong.theHordes.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.PDCHelper.PDCHelper;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.ArrayList;
import java.util.List;

import static org.zycong.theHordes.TheHordes.Colorize;

public class kits implements CommandHandler {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if (args.length == 0){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noArgs").toString()));
            return true;
        }
        if (!p.hasPermission("TheHordes.commands.kits")){
            p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "command.failed.noPermission").toString()));
            return true;
        }

        if (args[0].equalsIgnoreCase("create")){
            if (args.length == 2){
                yamlManager.getInstance().setOption("kits", args[1] + ".items", List.of());
                yamlManager.getInstance().setOption("kits", args[1] + ".icon", new ItemStack(Material.DIRT));
                openKitEditor(args[1], p);
            }
        }
        if (args[0].equalsIgnoreCase("edit")){
            if (args.length == 2){
                openKitEditor(args[1], p);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1){
            return List.of("create", "edit", "delete");
        } if (args.length == 2){
            if (args[1].equalsIgnoreCase("edit") || args[1].equalsIgnoreCase("delete")){
                List<Object> obs = yamlManager.getInstance().getNodes("kits", "");
                List<String> arg = new ArrayList<>();
                for (Object o : obs){
                    if (o instanceof String st) {arg.add(st); }
                }
                return arg;
            }
        }
        return List.of();
    }

    void openKitEditor(String kit, Player p){
        List<ItemStack> kits = (List<ItemStack>) yamlManager.getInstance().getOption("kits", kit + ".items");

        Inventory inv = Bukkit.createInventory(p, 36, "Kit Editor");
        for (ItemStack i : kits){
            inv.addItem(i);
        }
        p.openInventory(inv);
        p.setMetadata("inventory", new FixedMetadataValue(TheHordes.getPlugin(), "kitEditor"));
        p.setMetadata("editorType", new FixedMetadataValue(TheHordes.getPlugin(), kit));
    }

    public static void InventoryClose(InventoryCloseEvent event){
        Player p = (Player) event.getPlayer();
        String kit = p.getMetadata("editorType").get(0).asString();
        ItemStack[] items = event.getInventory().getContents();

        List<ItemStack> newItems = new ArrayList<>();
        for (ItemStack it : items){
            if (it != null){
                newItems.add(it);
            }
        }

        yamlManager.getInstance().setOption("kits", kit + ".items", newItems);
        p.removeMetadata("editorType", TheHordes.getPlugin());
    }

    public static void openSelector(Player p){
        Inventory selector = Bukkit.createInventory(p, 36, "Kits Selector");

        for (Object o : yamlManager.getInstance().getNodes("kits", "")){
            ItemStack item = (ItemStack) yamlManager.getInstance().getOption("kits", o + ".icon");
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Colorize("&f" + o.toString()));
            item.setItemMeta(meta);
            selector.addItem(item);
        }
        p.openInventory(selector);
        p.setMetadata("inventory", new FixedMetadataValue(TheHordes.getPlugin(), "kitSelector"));
    }
    public static void selectorUsed(InventoryClickEvent event){
        Player p =(Player) event.getWhoClicked();
        int selected = event.getRawSlot();
        int count = 0;
        for (Object o : yamlManager.getInstance().getNodes("kits", "")){
            if (count == selected){
                PDCHelper.setPlayerPDC("selectedKit", p, o.toString());
                p.removeMetadata("inventory", TheHordes.getPlugin());
                p.closeInventory();
                return;
            }
            count ++;
        }

    }
}