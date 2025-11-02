package org.zycong.theHordes.event.player;

import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.Lobby.lobbyManager;
import org.zycong.theHordes.commands.kits;

import static org.zycong.theHordes.helpers.PDCHelper.*;

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

    @EventHandler
    void onDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    void onBreak(BlockBreakEvent event){
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
            event.setCancelled(true);
        }
    }
    @EventHandler
    void onItemDamage(PlayerItemDamageEvent event){
        event.setCancelled(true);
    }
    @EventHandler
    void onAnvilDamage(AnvilDamagedEvent event){
        event.setCancelled(true);
    }
    @EventHandler
    void onCraft(CraftItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    void onChat(AsyncChatEvent event){
        Player p = event.getPlayer();
        if (!p.hasMetadata("GUIinput")) { return;}
        event.setCancelled(true);
        switch (p.getMetadata("GUIinput").get(0).asString())  {
            case ("kitPrice") : {
                TextComponent m = (TextComponent) event.message();
                kits.setPrice(m.content(), event.getPlayer());
                break;
            }
            case ("kitName") : {
                TextComponent m = (TextComponent) event.message();
                kits.setName(m.content(), event.getPlayer());
                break;
            }
        }
        p.removeMetadata("GUIinput", TheHordes.getPlugin());
    }

    @EventHandler
    void onDamage(EntityDamageByEntityEvent event){
        if (event.getDamager().getType() == EntityType.ARROW){
            if (event.getEntityType() == EntityType.PLAYER){
                event.setCancelled(true);
            }
        }
    }
}
