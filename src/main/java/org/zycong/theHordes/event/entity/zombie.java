package org.zycong.theHordes.event.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import org.zycong.theHordes.helpers.Lobby.lobbyManager;

import static org.zycong.theHordes.helpers.PDCHelper.*;

public class zombie implements Listener {
    @EventHandler
    void fire(EntityCombustEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    void onDead(EntityDeathEvent event){
        if (event.getEntityType() == EntityType.ZOMBIE){
            if (event.getEntity().getKiller() != null){
                Player p = event.getEntity().getKiller();
                int coins = 0;
                if (getPlayerPDC("coins", p) != null){
                    coins = Integer.parseInt(getPlayerPDC("coins", p));
                }
                coins++;
                setPlayerPDC("coins", p, String.valueOf(coins));
                if (lobbyManager.playerIsInGame(p)){
                    lobbyManager.zombieKilled(p);
                }
            }
        }
    }
}
