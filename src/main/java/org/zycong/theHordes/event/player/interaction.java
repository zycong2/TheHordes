package org.zycong.theHordes.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static org.zycong.theHordes.helpers.PDCHelper.PDCHelper.*;

public class interaction implements Listener {
    @EventHandler
    void onRightClick(PlayerInteractEvent event){
        Player p = event.getPlayer();
        Action a = event.getAction();
        ItemStack item = event.getItem();
        if ((a == Action.PHYSICAL) || (event.getItem() == null)) return;
        if (getItemPDC("event", item) == null) return;
        switch (getItemPDC("event", item)){
            case "startGame" :{

            }
        }



    }
}
