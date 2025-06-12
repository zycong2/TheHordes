package org.zycong.theHordes.event.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.zycong.theHordes.helpers.Lobby.lobbyManager;

public class playerDeath implements Listener {
    @EventHandler
    void playerDeathEvent(PlayerDeathEvent event){
        lobbyManager.playerAwayFromGame(event.getPlayer());
    }
    @EventHandler
    void playerRespawnEvent(PlayerRespawnEvent event){
        event.getPlayer().performCommand("spawn");
    }
}
