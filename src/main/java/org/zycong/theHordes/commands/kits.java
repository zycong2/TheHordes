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
                yamlManager.getInstance().setOptions("kits", args[1] + ".upgrades", Lic.of());
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
        meta1.displayName(ColorUtils.convertToComponent("&r&fKit cost in xp levels"));
        if (yamlManager.getInstance().getOption("kits", kit + ".price") != null) {
            meta1.lore(List.of(Colorize("&r&fPrice: " + yamlManager.getInstance().getOption("kits", kit + ".price"))));
        }
        cost.setItemMeta(meta1);
        inv.setItem(4, cost);

        ItemStack name = new ItemStack(Material.NAME_TAG);
        ItemMeta meta2 = name.getItemMeta();
        meta2.displayName(ColorUtils.convertToComponent("&r&fKit Name"));
        name.setItemMeta(meta2);
        inv.setItem(20, name);

        ItemStack items = new ItemStack(Material.CHEST);
        meta2 = items.getItemMeta();
        meta2.displayName(ColorUtils.convertToComponent("&r&fKit Items"));
        items.setItemMeta(meta2);
        inv.setItem(22, items);

        ItemStack logo = (ItemStack) yamlManager.getInstance().getOption("kits", kit + ".icon");
        meta2 = items.getItemMeta();
        meta2.displayName(ColorUtils.convertToComponent("Kit Item"));
        meta2.lore(List.of(Colorize("&rDrop items on the button to change the display item.")));
        items.setItemMeta(meta2);
        inv.setItem(24, logo);

        p.openInventory(inv);
        p.setMetadata("inventory", new FixedMetadataValue(TheHordes.getPlugin(), "kitsEditor"));
        p.setMetadata("kit", new FixedMetadataValue(TheHordes.getPlugin(), kit));
    }

    public static void editorUsed(InventoryClickEvent e){
        if (e.getRawSlot() == 22 || e.getRawSlot() == 4 || e.getRawSlot() == 20 || e.getRawSlot() == 24){
            e.getWhoClicked().removeMetadata("inventory", TheHordes.getPlugin());
        }
        if (e.getRawSlot() == 22){
            e.setCancelled(true);
            openKitsItemSelector(e.getWhoClicked().getMetadata("kit").get(0).asString() , (Player) e.getWhoClicked());
        } else if (e.getRawSlot() == 4){
            e.getWhoClicked().sendMessage(Colorize("&aPlease send the price of the kit in chat."));
            e.getWhoClicked().setMetadata("GUIinput", new FixedMetadataValue(TheHordes.getPlugin(), "kitPrice"));
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        } else if (e.getRawSlot() == 20){
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(Colorize("&aPlease send the new name of the kit in chat."));
            e.getWhoClicked().setMetadata("GUIinput", new FixedMetadataValue(TheHordes.getPlugin(), "kitName"));
            e.setCancelled(true);
        } else if (e.getRawSlot() == 24){
            if (e.getWhoClicked().getItemOnCursor().equals(new ItemStack(Material.AIR))) { return; }
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
        p.removeMetadata("inventory", TheHordes.getPlugin());
    }

    public static void setName(String name, Player p){
        String kit = p.getMetadata("kit").get(0).asString();

        yamlManager.getInstance().changeRoot("kits", kit, String.valueOf(name));
        p.setMetadata("GUIinput", new FixedMetadataValue(TheHordes.getPlugin(), null));
        p.sendMessage(Colorize("&aSuccesufly set new name."));
        p.removeMetadata("inventory", TheHordes.getPlugin());
        p.removeMetadata("kit", TheHordes.getPlugin());

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
            List<TextComponent> lore = new ArrayList<>(List.of());
            ItemStack item = (ItemStack) yamlManager.getInstance().getOption("kits", o + ".icon");
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Colorize("&f" + o.toString()));

            if (playerHasKit(p, o.toString() )){
                lore.add(Colorize("&aPurchased"));
            }
            else {
                lore.add(Colorize("&cNot purchased"));
                lore.add(Colorize("&fPrice: " + yamlManager.getInstance().getOption("kits", o + ".price")));
            }

            meta.lore(lore);
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
                if (playerHasKit(p, o.toString())) {
                    PDCHelper.setPlayerPDC("selectedKit", p, o.toString());
                    p.removeMetadata("inventory", TheHordes.getPlugin());
                    p.closeInventory();
                    return;
                } else {
                    event.setCancelled(true);
                    buyKit(p, o.toString());
                }
            }
            count ++;
        }
    }

    static boolean playerHasKit(Player p, String kit){
        if (PDCHelper.getPlayerPDC("boughtKits", p) == null){
            return yamlManager.getInstance().getOption("kits", kit + ".price") == null || (int) yamlManager.getInstance().getOption("kits", kit + ".price") == 0;
        } if(PDCHelper.getPlayerPDC("boughtKits", p).contains(kit)) {
            return true;
        } else {
            return yamlManager.getInstance().getOption("kits", kit + ".price") == null;
        }
    }

    static void buyKit(Player p, String kit){
        if (PDCHelper.getPlayerPDC("boughtKits", p) != null) {
            if (PDCHelper.getPlayerPDC("boughtKits", p).contains(kit)) {
                return;
            }
        }

        Inventory inv = Bukkit.createInventory(p, 27, "Do you want to buy the " + kit+ " kit?");


        ItemStack cancel = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta = cancel.getItemMeta();
        meta.displayName(Colorize("&cCancel"));
        cancel.setItemMeta(meta);
        inv.setItem(12, cancel);

        ItemStack buy = new ItemStack(Material.LIME_CONCRETE);
        meta = buy.getItemMeta();
        meta.displayName(Colorize("&aBuy for " + yamlManager.getInstance().getOption("kits", kit + ".price")));
        buy.setItemMeta(meta);
        inv.setItem(14, buy);

        p.openInventory(inv);
        p.setMetadata("inventory", new FixedMetadataValue(TheHordes.getPlugin(), "buyKit"));
        p.setMetadata("kit", new FixedMetadataValue(TheHordes.getPlugin(), kit));
    }

    public static void boughtGUIUsed(InventoryClickEvent e){
        if (e.getRawSlot() == 12){ openSelector((Player) e.getWhoClicked());}
        else if (e.getRawSlot() == 14){
            Player p = (Player) e.getWhoClicked();
            String kit = p.getMetadata("kit").get(0).asString();
            if (p.getLevel() >=  (int)yamlManager.getInstance().getOption("kits", kit + ".price")){
                p.setLevel(p.getLevel() - (int)yamlManager.getInstance().getOption("kits", kit + ".price"));

                String bought = "";
                if (PDCHelper.getPlayerPDC("boughtKits", p) != null){
                    bought = PDCHelper.getPlayerPDC("boughtKits", p);
                }
                PDCHelper.setPlayerPDC("boughtKits", p, bought + "," + kit);

                p.sendMessage(Colorize("&aYou bought the " + kit + " kit"));


                PDCHelper.setPlayerPDC("selectedKit", p, kit);
                p.removeMetadata("inventory", TheHordes.getPlugin());
                p.closeInventory();
            }else {
                p.sendMessage(Colorize("&cYou don't have enough experience to buy this kit."));
                p.closeInventory();
            }

        }
        e.getWhoClicked().removeMetadata("inventory", TheHordes.getPlugin());
        e.getWhoClicked().removeMetadata("kit", TheHordes.getPlugin());
    }
}
