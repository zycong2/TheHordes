package org.zycong.theHordes.event.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.commands.kits;

public class openClose implements Listener {
    @EventHandler
    void inventoryClose(InventoryCloseEvent event){
        Player p = (Player) event.getPlayer();
        if (p.hasMetadata("inventory")){
            String inventory = p.getMetadata("inventory").get(0).asString();
            switch(inventory){
                case "kitEditor" : {
                    kits.InventoryClose(event);
                }
            }
            p.removeMetadata("inventory", TheHordes.getPlugin());
        }
    }
}
