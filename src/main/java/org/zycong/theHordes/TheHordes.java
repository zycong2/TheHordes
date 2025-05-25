package org.zycong.theHordes;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.zycong.theHordes.helpers.ColorUtils;
import org.zycong.theHordes.helpers.PlaceHolder.PlaceholderAPI.DefensePlaceholder;
import org.zycong.theHordes.helpers.PlaceHolder.PlaceholderAPI.ManaPlaceholder;

import java.util.List;

public final class TheHordes extends JavaPlugin {

    public static List<String> yamlFiles = List.of("data", "messages", "mobDB", "config", "lootTables", "skills", "quests", "format");
    public static List<YamlConfiguration> fileConfigurationList = new java.util.ArrayList<>(List.of());

    public static boolean IsLuckperms = false;
    public static boolean IsPlaceholderAPI = false;

    @Override
    public void onEnable() {
        if(doesPluginExist("LuckPerms")){IsLuckperms = true;}
        if(doesPluginExist("PlaceholderAPI")){IsPlaceholderAPI = true;
            new DefensePlaceholder().register();
            new ManaPlaceholder().register();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() { return Bukkit.getServer().getPluginManager().getPlugin("TheHordes"); }

    public static TextComponent Colorize(String input){
        String s = FormatForMiniMessage(input);
        TextComponent deserialized = (TextComponent) MiniMessage.miniMessage().deserialize(s);
        return deserialized;
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
}
