package org.zycong.theHordes.helpers.Lobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.PDCHelper;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.kyori.adventure.bossbar.BossBar.bossBar;
import static org.zycong.theHordes.TheHordes.Colorize;

public class lobbyManager {
    static int maxPlayersInLobby = 16;
    static List<BossBar> timers = new ArrayList<>();
    static List<List<String>> games = new ArrayList<>();

    public static void startTimers(){
        timers.clear();
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        for (Object o : lobbies){
            timers.add(	bossBar(Colorize("&fStarting"), (float) 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_6).progress(BossBar.MIN_PROGRESS));
        }

        BukkitScheduler scheduler = TheHordes.getPlugin().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(TheHordes.getPlugin(), () -> {
            int count = 0;
            for (BossBar b : timers){
                if (b.progress() != BossBar.MIN_PROGRESS){
                    if (b.progress() - 1f / (int) yamlManager.getInstance().getOption("config", "lobbies.waitingTime") <= 0){
                        timers.set(count, b.progress(0));
                        startGame(count);
                        return;
                    }
                    timers.set(count, b.progress(b.progress() - 1f / (int) yamlManager.getInstance().getOption("config", "lobbies.waitingTime")));
                }
                count++;
            }
        }, 20L, 20L);

        scheduler.scheduleSyncRepeatingTask(TheHordes.getPlugin(), () -> {
            for (List l : games){
                String lobby = l.get(0).toString();
                if (yamlManager.getInstance().getOption("lobbies", lobby + ".spawnedAllZombies") == null) { spawnZombies(l); }
                else if (!(boolean) yamlManager.getInstance().getOption("lobbies", lobby + ".spawnedAllZombies")) { spawnZombies(l); }

            }
        }, 20L, 20L);
    }


    static void spawnZombies(List<String> l) {
        try {
            String lobby = l.get(0).toString();

            int difficulty = Integer.parseInt(l.get(1).toString()) + 1;
            int waves = (int) yamlManager.getInstance().getOption("lobbies", lobby + ".wave");

            Location lobbyLoc = TheHordes.stringToLocation((String) yamlManager.getInstance().getOption("lobbies", lobby + ".location"));
            List<String> spawnLocationsString = (List<String>) yamlManager.getInstance().getOption("lobbies", lobby + ".map.spawnLoc");

            List<Location> spawnLocations = new ArrayList<>();
            for (String s : spawnLocationsString) {
                spawnLocations.add(TheHordes.stringToLocation(s));
            }

            Bukkit.getLogger().info("spawning... difficulty: " + difficulty + " waves: " + waves);
            int maxSpawns = difficulty * waves;
            for (Object o : (List) yamlManager.getInstance().getOption("lobbies", lobby + ".players")) {
                if (o instanceof Player p) {
                    p.setGameMode(GameMode.ADVENTURE);
                    if (waves == 1) {
                        List<Entity> ents = p.getNearbyEntities(10, p.getWorld().getMaxHeight() * 2, 10);

                        for (Entity ent : ents) {
                            if (ent instanceof Monster) {
                                ((Monster) ent).setHealth(0);
                            }
                        }
                    }
                    List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
                    int lNumber = 0;
                    for (Object ob : lobbies) {
                        if (ob.toString().equals(lobby)) {
                            p.hideBossBar(timers.get(lNumber));
                        }
                        lNumber++;
                    }

                }
            }
            Bukkit.getLogger().info("spawneding max " + maxSpawns + " zombies spawnlocations: " + spawnLocations.size());
            for (int i = 0; i < maxSpawns + 1; i++) {
                Random rand = new Random();
                Entity entity = spawnLocations.get(0).getWorld().spawnEntity(spawnLocations.get(rand.nextInt(spawnLocations.size())), EntityType.ZOMBIE);
                entity.setCustomNameVisible(false);
                entity.setCustomName("zombie lvl " + waves);
                Zombie zomb = (Zombie) entity;
                zomb.setAdult();
                LivingEntity le = (LivingEntity) entity;
                le.setMaxHealth(20 + waves);
                le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue() + difficulty);
                le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() + difficulty*0.005);


            }
            yamlManager.getInstance().setOption("lobbies", lobby + ".spawnedAllZombies", true);
            yamlManager.getInstance().setOption("lobbies", lobby + ".zombieCount", maxSpawns -1);
        } catch (NullPointerException exeption) {
            games.remove(l);
        }
    }

    public static void addToLobby(Player p){
        p.getInventory().clear();
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        maxPlayersInLobby = lobbies.size();
        int lNumber = 0;
        for (Object o : lobbies){
            Object playerObject = yamlManager.getInstance().getOption("lobbies", o + ".players");
            if (playerObject == null) {playerObject = List.of(); }
            if (playerObject instanceof List players){
                if (players.size() <= 16) {
                    if (yamlManager.getInstance().getOption("lobbies", o + ".wave") == null) {
                        if (players.contains(p)) {
                            return;
                        }

                        ItemStack kitSelect = new ItemStack(Material.IRON_SWORD);
                        ItemMeta meta = kitSelect.getItemMeta();
                        meta.displayName(Colorize("&aSelect kit"));
                        kitSelect.setItemMeta(meta);
                        PDCHelper.setItemPDC("events", kitSelect, "kits");
                        p.getInventory().addItem(kitSelect);

                        List<Player> newPlayers = new ArrayList<>();
                        newPlayers.addAll(players);
                        newPlayers.add(p);

                        yamlManager.getInstance().setOption("lobbies", o + ".players", newPlayers);
                        p.sendMessage(Colorize(yamlManager.getInstance().getOption("messages", "event.success.addedToLobby").toString()));
                        Location loc = TheHordes.stringToLocation((String) yamlManager.getInstance().getOption("lobbies", o + ".location"));
                        p.teleport(loc);
                        p.setGameMode(GameMode.ADVENTURE);
                        p.setHealth(20);
                        p.setFoodLevel(20);
                        if (newPlayers.size() == 1) {
                            try {
                                timers.get(lNumber).progress(BossBar.MAX_PROGRESS);
                            } catch (IndexOutOfBoundsException e) {
                                startTimers();
                                timers.get(lNumber).progress(BossBar.MAX_PROGRESS);
                            }
                        }
                        p.showBossBar(timers.get(lNumber));
                        return;
                    }
                }
            }
            lNumber ++;
        }
    }

    public static void clearLobbies(){
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        games.clear();
        for (Object o : lobbies){
            yamlManager.getInstance().setOption("lobbies", o + ".players", List.of());
            yamlManager.getInstance().setOption("lobbies", o + ".killed", null);
            yamlManager.getInstance().setOption("lobbies", o + ".zombieCount", null);
            yamlManager.getInstance().setOption("lobbies", o + ".spawnedAllZombies", null);
            yamlManager.getInstance().setOption("lobbies", o + ".wave", null);
        }
    }

    public static void startGame(int count){
        int currentCount = 0;
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        for (Object o : lobbies) {
            if (currentCount == count) {
                Location loc = TheHordes.stringToLocation((String) yamlManager.getInstance().getOption("lobbies", o + ".map.location"));
                List playerObjects = (List) yamlManager.getInstance().getOption("lobbies", o + ".players");
                for (Object ob : playerObjects){
                    if (ob instanceof Player p){
                        p.teleport(loc);
                        p.setGameMode(GameMode.ADVENTURE);
                        p.getInventory().clear();
                        if (PDCHelper.getPlayerPDC("selectedKit", p) == null){
                            PDCHelper.setPlayerPDC("selectedKit", p, yamlManager.getInstance().getNodes("kits", "").get(0).toString());
                        }
                        for (ItemStack item : (List<ItemStack>)yamlManager.getInstance().getOption("kits", PDCHelper.getPlayerPDC("selectedKit", p) + ".items")){
                            p.getInventory().addItem(item);
                        }
                    }
                }
                yamlManager.getInstance().setOption("lobbies", o + ".wave", 1);
                if (count == 0){
                    games.add(List.of(o.toString(), String.valueOf(count+1)));
                }else {
                    games.add(List.of(o.toString(), String.valueOf(count)));
                }
            }
            currentCount ++;
        }
    }

    public static boolean playerIsInGame(Player p){
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        for (Object o : lobbies) {
            List playerObjects = (List) yamlManager.getInstance().getOption("lobbies", o + ".players");
            for (Object ob : playerObjects){
                if (ob instanceof Player pla){
                    if (p == pla){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getPlayerGame(Player p){
        List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
        for (Object o : lobbies) {
            List playerObjects = (List) yamlManager.getInstance().getOption("lobbies", o + ".players");
            for (Object ob : playerObjects){
                if (ob instanceof Player pla){
                    if (p == pla){
                        return o.toString();
                    }
                }
            }
        }
        return null;
    }

    public static void zombieKilled(Player p){
        String lobby = getPlayerGame(p);
        int kills = 0;
        if (yamlManager.getInstance().getOption("lobbies", lobby + ".killed") == null){
        }else {
            kills = (int) yamlManager.getInstance().getOption("lobbies", lobby + ".killed");
        }

        kills++;
        yamlManager.getInstance().setOption("lobbies", lobby + ".killed", kills); //zombieCount
        if ((int)yamlManager.getInstance().getOption("lobbies", lobby + ".zombieCount") <= kills) {
            int wave = (int) yamlManager.getInstance().getOption("lobbies", lobby + ".wave");
            wave++;
            yamlManager.getInstance().setOption("lobbies", lobby + ".wave", wave);
            List<Player> players = (List<Player>) yamlManager.getInstance().getOption("lobbies", lobby + ".players");
            for (Player pl : players){
                pl.showTitle(Title.title(
                        Component.text("Wave " + wave), Component.text(""),
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
                ));
            }
            yamlManager.getInstance().setOption("lobbies", lobby + ".spawnedAllZombies", false);
            yamlManager.getInstance().setOption("lobbies", lobby + ".killed", null);
        }
    }

    public static void playerAwayFromGame(Player p){
        if (playerIsInGame(p)){
            String lobby = getPlayerGame(p);
            List players = (List) yamlManager.getInstance().getOption("lobbies", lobby + ".players");
            players.remove(p);

            if (players.isEmpty()){
                resetMap(lobby);
            } else {
                yamlManager.getInstance().setOption("lobbies", lobby + ".players", players);
            }
        }
    }

    public static void resetMap(String o){
        try {
            int currentCount = 0;
            List<Object> lobbies = yamlManager.getInstance().getNodes("lobbies", "");
            for (Object ob : lobbies) {
                if (ob.toString().equals(o)) {
                    games.remove(List.of(o, yamlManager.getInstance().getOption("lobbies", o + ".wave")));
                }
            }
            yamlManager.getInstance().setOption("lobbies", o + ".players", List.of());
            yamlManager.getInstance().setOption("lobbies", o + ".killed", null);
            yamlManager.getInstance().setOption("lobbies", o + ".zombieCount", null);
            yamlManager.getInstance().setOption("lobbies", o + ".spawnedAllZombies", null);
            yamlManager.getInstance().setOption("lobbies", o + ".wave", null);
            List<String> spawnLocationsString = (List<String>) yamlManager.getInstance().getOption("lobbies", o + ".map.spawnLoc");

            List<Location> spawnLocations = new ArrayList<>();
            for (String s : spawnLocationsString) {
                spawnLocations.add(TheHordes.stringToLocation(s));
            }
            for (Entity e : spawnLocations.get(0).getWorld().getEntities()) {
                if (e.getType() == EntityType.ZOMBIE) {
                    LivingEntity le = (LivingEntity) e;
                    le.setHealth(0);
                }
            }
            for (Entity e : spawnLocations.get(0).getWorld().getEntities()) {
                if (e.getType() == EntityType.DROPPED_ITEM) {
                    LivingEntity le = (LivingEntity) e;
                    le.setHealth(0);
                }
            }
        } catch (NullPointerException ignored) { }
    }
}
