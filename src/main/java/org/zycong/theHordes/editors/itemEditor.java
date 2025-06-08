package org.zycong.theHordes.editors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zycong.theHordes.helpers.GUI.Editor.editorInterface;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.List;

public class itemEditor implements editorInterface {
    @Override
    public Inventory mainLayout(Player p) {
        /*Inventory menu = Bukkit.createInventory(p, 45, "ItemDB");
        List<ItemStack> items = yamlManager.getInstance().getCustomItems();

        if (items.size() <= 36) {
            for (int i = 0; i < items.size(); ++i) {
                menu.setItem(i, items.get(i));
            }
        } else {
            int page = p.hasMetadata("itemDBPage") ? p.getMetadata("itemDBPage").getFirst().asInt() : 0;
            for (int i = 0; i <= 36 && i + 36 * page < items.size(); ++i) {
                menu.setItem(i, items.get(i + 36 * page));
            }
        }

        // Add pagination arrows
        ItemStack nextArrow = new ItemStack(Material.ARROW);
        ItemMeta meta = nextArrow.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aNext"));
        nextArrow.setItemMeta(meta);

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta meta2 = nextArrow.getItemMeta();
        meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aBack"));
        backArrow.setItemMeta(meta2);

        menu.setItem(39, backArrow);
        menu.setItem(41, nextArrow);

        ItemStack newItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta meta3 = newItem.getItemMeta();
        meta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aCreate a new item"));
        newItem.setItemMeta(meta3);

        menu.setItem(44, newItem);

        return menu;*/
        return null;
    }

    @Override
    public Inventory editorLayout(Player p) {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }

    @Override
    public String id() {
        return "";
    }
}
