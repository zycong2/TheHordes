package org.zycong.theHordes.event.player;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zycong.theHordes.helpers.Lobby.lobbyManager;

import static org.zycong.theHordes.helpers.ColorUtils.colorize;
import static org.zycong.theHordes.helpers.PDCHelper.PDCHelper.setItemPDC;

public class playerDeath implements Listener {
    @EventHandler
    void playerDeathEvent(PlayerDeathEvent event){
        lobbyManager.playerAwayFromGame(event.getPlayer());
    }
    @EventHandler
    void playerRespawnEvent(PlayerRespawnEvent event){
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        event.getPlayer().getInventory().clear();
        ItemStack startGame = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta meta = startGame.getItemMeta();
        meta.setDisplayName(colorize("&aStart game!", '&'));
        startGame.setItemMeta(meta);
        setItemPDC("events", startGame, "startGame");
        event.getPlayer().getInventory().setItem(8, startGame);
        event.getPlayer().performCommand("spawn");
    }
}
