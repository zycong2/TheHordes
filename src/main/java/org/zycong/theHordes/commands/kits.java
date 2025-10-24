package org.zycong.theHordes.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.ColorUtils;
import org.zycong.theHordes.helpers.PDCHelper;
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
            if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("delete")){
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

    static void openKitEditor(String kit, Player p){
        if (yamlManager.getInstance().getOption("kits", kit + ".icon") == null){
            return;
        }

        Inventory inv = Bukkit.createInventory(p, 36, "Kit Editor");

        ItemStack cost = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta1 = cost.getItemMeta();
        meta1.displayName(ColorUtils.convertToComponent("Kit cost in xp levels"));
        if (yamlManager.getInstance().getOption("kits", kit + ".price") != null) {
            meta1.setLore(List.of("Price: " + yamlManager.getInstance().getOption("kits", kit + ".price")));
        }
        cost.setItemMeta(meta1);
        inv.setItem(4, cost);

        ItemStack name = new ItemStack(Material.NAME_TAG);
        ItemMeta meta2 = name.getItemMeta();
        meta2.displayName(ColorUtils.convertToComponent("Kit Name"));
        name.setItemMeta(meta2);
        inv.setItem(20, name);

        ItemStack items = new ItemStack(Material.CHEST);
        meta2 = items.getItemMeta();
        meta2.displayName(ColorUtils.convertToComponent("Kit Items"));
        items.setItemMeta(meta2);
        inv.setItem(22, items);

        ItemStack logo = (ItemStack) yamlManager.getInstance().getOption("kits", kit + ".icon");
        meta2 = items.getItemMeta();
        meta2.displayName(ColorUtils.convertToComponent("Kit Item"));
        meta2.setLore(List.of("&rDrop items on the button to change the display item."));
        items.setItemMeta(meta2);
        inv.setItem(24, logo);

        p.openInventory(inv);
        p.setMetadata("inventory", new FixedMetadataValue(TheHordes.getPlugin(), "kitsEditor"));
        p.setMetadata("kit", new FixedMetadataValue(TheHordes.getPlugin(), kit));
    }

    public static void editorUsed(InventoryClickEvent e){

        if (e.getRawSlot() == 22){
            openKitsItemSelector(e.getWhoClicked().getMetadata("kit").get(0).asString() , (Player) e.getWhoClicked());
            e.setCancelled(true);
        } else if (e.getRawSlot() == 4){
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(Colorize("&aPlease send the price of the kit in chat."));
            e.getWhoClicked().setMetadata("GUIinput", new FixedMetadataValue(TheHordes.getPlugin(), "kitPrice"));
            e.setCancelled(true);
        } else if (e.getRawSlot() == 20){
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(Colorize("&aPlease send the new name of the kit in chat."));
            e.getWhoClicked().setMetadata("GUIinput", new FixedMetadataValue(TheHordes.getPlugin(), "kitName"));
            e.setCancelled(true);
        } else if (e.getRawSlot() == 24){
            e.getWhoClicked().getItemOnCursor();
            String kit = e.getWhoClicked().getMetadata("kit").get(0).asString();
            yamlManager.getInstance().setOption("kits", kit + ".icon", e.getWhoClicked().getItemOnCursor());
            e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        }
    }

    public static void setPrice(String message, Player p){
        try{
            int amount = Integer.parseInt(message);
            String kit = p.getMetadata("kit").get(0).asString();

            yamlManager.getInstance().setOption("kits", kit + ".price", amount);
            p.setMetadata("GUIinput", new FixedMetadataValue(TheHordes.getPlugin(), null));
            p.sendMessage(Colorize("&aSuccesufly set new price."));

        }
        catch (NumberFormatException e){
            p.sendMessage(Colorize("&cPlease only send a number!"));
        }
    }

    public static void setName(String name, Player p){
        String kit = p.getMetadata("kit").get(0).asString();

        yamlManager.getInstance().changeRoot("kits", kit, String.valueOf(name));
        p.setMetadata("GUIinput", new FixedMetadataValue(TheHordes.getPlugin(), null));
        p.sendMessage(Colorize("&aSuccesufly set new name."));

    }

    static void openKitsItemSelector(String kit, Player p){
        Inventory inv = Bukkit.createInventory(p, 36, "Kit Item Editor");

        if (yamlManager.getInstance().getOption("kits", kit + ".items") != null) {
            List<ItemStack> kits = (List<ItemStack>) yamlManager.getInstance().getOption("kits", kit + ".items");
            for (ItemStack i : kits) {
                inv.addItem(i);
            }
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