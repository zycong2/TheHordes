package org.zycong.theHordes.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import org.zycong.theHordes.helpers.Lobby.lobbyManager;
import org.zycong.theHordes.commands.kits;

import static org.zycong.theHordes.helpers.PDCHelper.PDCHelper.*;

public class interaction implements Listener {
    @EventHandler
    void onRightClick(PlayerInteractEvent event){
        Player p = event.getPlayer();
        Action a = event.getAction();
        ItemStack item = event.getItem();
        if ((event.getItem() == null)) return;
        if (getItemPDC("events", item) == null) return;
        event.setCancelled(true);
        switch (getItemPDC("events", item)){
            case "startGame" :{
                lobbyManager.addToLobby(p);
            } case "kits" : {
                kits.openSelector(p);
            }
        }




    }
}
