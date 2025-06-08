package org.zycong.theHordes.helpers.GUI.Editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface editorInterface {
    Inventory mainLayout(Player p);
    Inventory editorLayout(Player p);
    void onClick(InventoryClickEvent event);
    String id();
}
