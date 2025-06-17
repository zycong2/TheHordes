package org.zycong.theHordes;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.zycong.theHordes.commands.CommandRegister.CommandRegister;
import org.zycong.theHordes.event.entity.zombie;
import org.zycong.theHordes.event.player.interaction;
import org.zycong.theHordes.event.player.playerConnect;
import org.zycong.theHordes.event.player.playerDeath;
import org.zycong.theHordes.helpers.ColorUtils;
import org.zycong.theHordes.helpers.GUI.GUI.GUI;
import org.zycong.theHordes.helpers.GUI.GUI.GUIListener;
import org.zycong.theHordes.helpers.PlaceHolder.PlaceholderAPI.DefensePlaceholder;
import org.zycong.theHordes.helpers.PlaceHolder.PlaceholderAPI.ManaPlaceholder;
import org.zycong.theHordes.helpers.commandHelper.CommandManager;
import org.zycong.theHordes.helpers.yaml.yamlManager;
import org.zycong.theHordes.helpers.Lobby.lobbyManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TheHordes extends JavaPlugin {

    public static List<String> yamlFiles = List.of("data", "messages", "config", "lobbies");
    public static List<YamlConfiguration> fileConfigurationList = new java.util.ArrayList<>(List.of());

    public static Map<String, YamlConfiguration> ItemDB = new HashMap<>();

    public static List<LivingEntity> customMobs = new java.util.ArrayList<>(List.of());
    public static List<String> spawns = new java.util.ArrayList<>(List.of());

    public static List<String> DBFolders = List.of("itemDB", "mobDB", "GUI");
    public static Map<String, List<YamlConfiguration>> DBFileConfiguration = new HashMap<>();

    public static boolean IsLuckperms = false;
    public static boolean IsPlaceholderAPI = false;

    @Override
    public void onEnable() {
        Logger reflectionsLogger = Logger.getLogger("org.reflections");
        reflectionsLogger.setLevel(Level.OFF);

        yamlManager.getInstance().loadData();
        lobbyManager.startTimers();

        if(doesPluginExist("LuckPerms")){IsLuckperms = true;}
        if(doesPluginExist("PlaceholderAPI")){IsPlaceholderAPI = true;
            new DefensePlaceholder().register();
            new ManaPlaceholder().register();
        }

        new CommandRegister(this);

        try (ScanResult result = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages("org.zycong.theHordes") // path
                .scan()) {

            for (ClassInfo info : result.getClassesWithAnnotation(CommandRegister.AutoRegisterer.class.getName())) {
                Class<?> clazz = info.loadClass();
                Object instance = clazz.getDeclaredConstructor().newInstance();
                CommandRegister.global().registerCommands(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerListeners(
            new interaction(),
            new playerConnect(),
            new zombie(),
            new playerDeath(),
            new GUIListener()
        );

        this.getCommand("TheHordes").setExecutor(new CommandManager());
        this.getCommand("TheHordes").setTabCompleter(new CommandManager());


    }

    @Override
    public void onDisable() {
        yamlManager.getInstance().saveData();
        lobbyManager.clearLobbies();
    }

    public static Plugin getPlugin() { return Bukkit.getServer().getPluginManager().getPlugin("TheHordes"); }

    public static TextComponent Colorize(String input){
        String s = FormatForMiniMessage(input);
        return (TextComponent) MiniMessage.miniMessage().deserialize(s);
    }

    public static String FormatForMiniMessage(String input){
        String output = input.replace("&0", "<black>").replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>").replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>").replace("&5", "<dark_purple>")
                .replace("&6", "<gold>").replace("&7", "<gray>")
                .replace("&8", "<dark_gray>").replace("&9", "<blue>")
                .replace("&a", "<green>").replace("&b", "<aqua>")
                .replace("&c", "<red>").replace("&d", "<light_purple>")
                .replace("&e", "<yellow>").replace("&f", "<white>")
                .replace("&l", "<bold>").replace("&m", "<strikethrough>")
                .replace("&n", "<underline>").replace("&o", "<italic>")
                .replace("&r", "<reset>")
                .replace("§0", "<black>").replace("§1", "<dark_blue>")
                .replace("§2", "<dark_green>").replace("§3", "<dark_aqua>")
                .replace("§4", "<dark_red>").replace("§5", "<dark_purple>")
                .replace("§6", "<gold>").replace("§7", "<gray>")
                .replace("§8", "<dark_gray>").replace("§9", "<blue>")
                .replace("§a", "<green>").replace("§b", "<aqua>")
                .replace("§c", "<red>").replace("§d", "<light_purple>")
                .replace("§e", "<yellow>").replace("§f", "<white>")
                .replace("§l", "<bold>").replace("§m", "<strikethrough>")
                .replace("§n", "<underline>").replace("§o", "<italic>")
                .replace("§r", "<reset>");
        return output;
    }
    public boolean doesPluginExist(String pluginName) {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
    public static String ColorizeReString(String input) {
        return ColorUtils.colorize(input, '&');
    }

    private void registerListeners(Listener... l) {
        Arrays.asList(l).forEach(I-> getServer().getPluginManager().registerEvents(I, this));
    }
}
