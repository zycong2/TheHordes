package org.zycong.theHordes.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zycong.theHordes.helpers.Lobby.lobbyManager;

import static org.zycong.theHordes.helpers.ColorUtils.colorize;
import static org.zycong.theHordes.helpers.PDCHelper.PDCHelper.*;

public class playerConnect implements Listener {
    @EventHandler
    void onJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        p.performCommand("spawn");
        p.getInventory().clear();

        ItemStack startGame = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta meta = startGame.getItemMeta();
        meta.setDisplayName(colorize("&aStart game!", '&'));
        startGame.setItemMeta(meta);
        setItemPDC("events", startGame, "startGame");
        p.getInventory().setItem(8, startGame);
    }
    @EventHandler
    void onQuit(PlayerQuitEvent event){
        lobbyManager.playerAwayFromGame(event.getPlayer());
    }
}
