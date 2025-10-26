package org.zycong.theHordes.event.player.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.zycong.theHordes.commands.kits;

public class click implements Listener {
    @EventHandler
    void onGuiClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        if (p.hasMetadata("inventory")){
            String inventory = p.getMetadata("inventory").get(0).asString();

            switch(inventory){
                 case "kitSelector" : {
                    kits.selectorUsed(event);
                    event.setCancelled(true);
                     break;
                }
                case "kitsEditor" : {
                    kits.editorUsed(event);
                    break;
                }
                case "buyKit" : {
                    kits.boughtGUIUsed(event);
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}
