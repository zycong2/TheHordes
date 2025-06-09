package org.zycong.theHordes.event.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class zombieBurn implements Listener {
    @EventHandler
    void fire(EntityCombustEvent event){
        event.setCancelled(true);
    }
}
