package org.zycong.theHordes.helpers.Lobby;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.scheduler.BukkitScheduler;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.List;

import static net.kyori.adventure.bossbar.BossBar.bossBar;
import static org.zycong.theHordes.TheHordes.Colorize;

public class lobbyManager {
    static int maxPlayersInLobby = 16;
    static List<BossBar> timers = List.of();

    public void startTimers(){
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        for (Object o : lobbies){
            timers.add(	bossBar(Colorize("&fStarting"), (float) 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_6).progress(BossBar.MIN_PROGRESS));
        }

        BukkitScheduler scheduler = TheHordes.getPlugin().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(TheHordes.getPlugin(), () -> {
            int count = 0;
            for (BossBar b : timers){
                if (b.progress() != BossBar.MIN_PROGRESS){
                    timers.set(count, b.progress(b.progress() - 1f / (int) yamlManager.getInstance().getOption("config", "lobbies.waitingTime")));
                }
                count++;
            }

        }, 20L, 20L);
    }

    public static void addToLobby(Player p){
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        maxPlayersInLobby = lobbies.size();
        int lNumber = 0;
        for (Object o : lobbies){
            Object playerObject = yamlManager.getInstance().getOption("lobbies", o + ".players");
            if (playerObject == null) {playerObject = List.of(); }
            if (playerObject instanceof List players){
                if (players.size() <= 16) {
                    if (players.contains(p)) {
                        return;
                    }
                    players.add(p);
                    yamlManager.getInstance().setOption("lobbies", o + ".players", players);
                    p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "event.success.addedToLobby").toString()));
                    Location loc = (Location) yamlManager.getInstance().getOption("lobbies", o + ".location");
                    p.teleport(loc);
                    if (players.size() == 1){
                        timers.get(lNumber).progress(BossBar.MAX_PROGRESS);
                    }
                    p.showBossBar(timers.get(lNumber));
                    return;
                }
            }
            lNumber ++;
        }
    }

    public static void clearLobbies(){
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        for (Object o : lobbies){
            yamlManager.getInstance().setOption("lobbies", o + ".players", List.of());
        }
    }
}
