package org.zycong.theHordes.helpers.yaml;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.GUI.GUI.GUIItem;
import org.zycong.theHordes.helpers.PDCHelper;
import org.zycong.theHordes.helpers.Traders.MobNPC;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.zycong.theHordes.TheHordes.*;

public class yamlManager {
    private static FileConfiguration fileConfig;
    private static File cfile;

    private static yamlManager myInstance;

    private yamlManager() {}

    public static yamlManager getInstance() {
        if (myInstance == null){
            myInstance = new yamlManager();
        }
        return myInstance;
    }

    public boolean defaultConfig() {
        for (String config : TheHordes.yamlFiles) {
            cfile = new File(TheHordes.getPlugin().getDataFolder().getAbsolutePath(), config + ".yml");
            fileConfig = new YamlConfiguration();

            if (!cfile.exists()){
                try {
                    cfile.createNewFile();
                } catch (IOException ignored) { }
            }
        }
        return setDefaults();
    }

    public synchronized void saveData() {
        boolean ok = true;
        for (String config : TheHordes.yamlFiles) {
            cfile = new File(TheHordes.getPlugin().getDataFolder().getAbsolutePath(), config + ".yml");
            try {
                getFileConfig(config).save(cfile);
            } catch (IOException ignored) { ok = false;}
        }
    }

    public void loadData() {
        for (String config : TheHordes.yamlFiles) {
            TheHordes.fileConfigurationList.add(new YamlConfiguration());
            cfile = new File(TheHordes.getPlugin().getDataFolder().getAbsolutePath(), config + ".yml");
            if (cfile.exists()) {
                getFileConfig(config) ;
                int index = 0;
                for (String s : TheHordes.yamlFiles) {
                    if (Objects.equals(s, config)) {break;}
                    index++;
                }

                TheHordes.fileConfigurationList.set(index, YamlConfiguration.loadConfiguration(cfile));
            }
        }

        for(String s : DBFolders){
            File file = new File(TheHordes.getPlugin().getDataFolder().getAbsolutePath(), s);
            File defaultFile = new File(file.getAbsolutePath(), "Default.yml");

            if (!file.exists()){file.mkdirs();}
            if(!defaultFile.exists()){try{defaultFile.createNewFile();}catch (Exception e){}}

            List<YamlConfiguration> list = new ArrayList<>();
            getAllFilesConfig(file, list);
            DBFileConfiguration.put(s, list);
        }
        defaultConfig();
    }

    public YamlConfiguration getFileConfig(String ymlFile) {
        int index = 0;
        for (String s : TheHordes.yamlFiles) {
            if (Objects.equals(s, ymlFile)) {
                return TheHordes.fileConfigurationList.get(index);
            }
            index++;
        } for (String s : DBFolders) {
            if (Objects.equals(s, ymlFile)) {
                return TheHordes.fileConfigurationList.get(index);
            }
            index++;
        }
        return null;
    }

    public void setOption(String file, String path, Object option){ getFileConfig(file).set(path, option); }

    public void getAllFilesConfig(File f, List<YamlConfiguration> list){
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                if (file.isDirectory()) {
                    getAllFilesConfig(file, list);
                } else {
                    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    list.add(yaml);
                }
            }
        }
    }

    public Object getOption(String file, String path){
        if (getFileConfig(file).get(path) == null){ return null; }
        Object data = getFileConfig(file).get(path);
        return data;
    }

    public List<Object> getNodes(String file, String path) {
        Set<String> nodes = yamlManager.getInstance().getFileConfig(file).getConfigurationSection(path).getKeys(false);
        return new ArrayList<>(nodes);
    }
    public boolean setDefaults() {
        if (getFileConfig("messages").getDefaults() == null) {
            getFileConfig("messages").addDefault("command.failed.noArgs", "&cPlease specify arguments");
            getFileConfig("messages").addDefault("command.failed.noPermission", "&cYou don't have permission for this command!");

            getFileConfig("messages").addDefault("command.success.spawn.set", "&aSuccessfully set the new spawn!");
            getFileConfig("messages").addDefault("command.success.spawn.tp", "&aSuccessfully teleported to spawn!");
            getFileConfig("messages").addDefault("command.success.lobbies.create", "&aSuccessfully made the new lobby at your location!");
            getFileConfig("messages").addDefault("command.success.lobbies.delete", "&aSuccessfully deleted the lobby!");
            getFileConfig("messages").addDefault("command.success.lobbies.location.added", "&aSuccessfully added your location as spawn point!");

            getFileConfig("messages").addDefault("event.success.addedToLobby", "&aYou successfully joined a lobby!");
            getFileConfig("messages").options().copyDefaults(true);
        }

        if (getFileConfig("config").getDefaults() == null) {
            Location loc = new Location(Bukkit.getWorld("world"), 0, 100, 0);
            getFileConfig("config").addDefault("spawn.location", loc);

            getFileConfig("config").addDefault("lobbies.waitingTime", 30);
            getFileConfig("config").options().copyDefaults(true);
        }
        return true;
    }
    
    public List<Object> getAllNodesInDB(String DBFolder){
        if(DBFileConfiguration.get(DBFolder) == null){ return null; }
        List<Object> nodes = new ArrayList<>();
        for (YamlConfiguration yaml : DBFileConfiguration.get(DBFolder)) {
            List<Object> node = List.of(yaml.getKeys(false));
            for (Object s : node) {
                if (!nodes.contains(s)) {
                    nodes.add(s);
                }
            }
        }
        return nodes;
    }
    
    public List<ItemStack> getCustomItems() {
        List<ItemStack> items = new ArrayList();
        List<Object> nodes = getAllNodesInDB("itemDB");
        for (Object node : nodes) {
            String key = node.toString();
            items.add(getItem(key));
        }

        return items;
    }

    public ItemStack getItem(String name) {
        List<YamlConfiguration> itemDB = DBFileConfiguration.get("itemDB");
        YamlConfiguration itemFile = null;
        for (YamlConfiguration yaml : itemDB) {
            if (yaml.get(name + ".ItemID") != null) {
                itemFile = yaml;
                break;
            }
        }
        if(itemFile == null) {
            Bukkit.getLogger().info("Item not found in itemDB");
            return null;
        }
        if(itemFile.getString(name + ".ItemID") == null){
            Bukkit.getLogger().info("Item does not have a ID");
            return null;
        }
        Material itemType = Material.getMaterial((String) Objects.requireNonNull(itemFile.get(name + ".itemType")));
        if (itemType == null) {
            Logger var10000 = Bukkit.getLogger();
            String var42 = String.valueOf(itemFile.get(name + ".itemType"));
            var10000.severe("Could not find material " + var42 + " " + name);
            return null;
        } else {
            ItemStack item = new ItemStack(itemType);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList(List.of());
            List<String> PDC = new ArrayList(List.of());
            PDC.add("ItemID;" + itemFile.getString(name + ".ItemID"));
            if(!(ItemDB.containsKey(itemFile.getString(name + ".ItemID")))){
                ItemDB.put(itemFile.getString(name + ".ItemID"), itemFile);
            }
            int attributes = 0;

            if (attributes != 0) {
                lore.add("");
                lore.add(0, "");
            }

            if (isItemSet(name + ".name", itemFile)) {
                meta.setDisplayName(ColorizeReString(itemFile.getString(name + ".name")));
            }

            if (isItemSet(name + ".customModelData", itemFile)) {
                meta.setCustomModelData((Integer)itemFile.get(name + ".customModelData"));
            }

            if (isItemSet(name + ".enchantments", itemFile)) {
                for(Object enchantmentString : Objects.requireNonNull(itemFile.getStringList(name + ".enchantments"))) {
                    String[] enchantString = enchantmentString.toString().split(":");
                    Enchantment enchantment = Enchantment.getByName(enchantString[0]);
                    meta.addEnchant(enchantment, Integer.valueOf(enchantString[1]), true);
                }
            }
            if (isItemSet(name + ".hide", itemFile)) {
                for(Object hide : (List)itemFile.get(name + ".hide")) {
                    meta.addItemFlags(ItemFlag.valueOf("HIDE_" + hide));
                }
            }

            if (isItemSet(name + ".lore", itemFile)) {
                if (isConfigSet("items.lore.prefix")) {
                    String config = ColorizeReString((String) yamlManager.getInstance().getOption("config","items.lore.prefix"));
                    lore.add(config);
                }

                for (String str : itemFile.getStringList(name + ".lore")){
                    lore.add(ColorizeReString(str));
                }
                if (isConfigSet("items.lore.suffix")) {
                    String config = ColorizeReString((String) yamlManager.getInstance().getOption("config","items.lore.suffix"));
                    lore.add(config);
                }
            }

            if (isItemSet(name + ".rarity", itemFile)) {
                lore.add("");
                lore.add(ColorizeReString(getFileConfig("config").getString("items.display.rarity." + itemFile.get(name + ".rarity"))));
                lore.add("");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
            if (meta instanceof LeatherArmorMeta leatherMeta) {
                if (isItemSet(name + ".color", itemFile)) {
                    String[] colors = String.valueOf(itemFile.get(name + ".color")).split(",");
                    Color color = Color.fromARGB(1, Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
                    leatherMeta.setColor(color);
                }

                item.setItemMeta(leatherMeta);
            } else if (meta instanceof BookMeta bookMeta) {
                if (isItemSet(name + ".title", itemFile)) {
                    bookMeta.setTitle((String)itemFile.get(name + ".title"));
                }

                if (isItemSet(name + ".author", itemFile)) {
                    bookMeta.setAuthor((String)itemFile.get(name + ".author"));
                }

                if (isItemSet(name + ".pages", itemFile)) {
                    bookMeta.setPages((List)itemFile.get(name + ".pages"));
                }
            }

            if (itemFile.get(name + ".recipe.permission") != null){
                String permission = (String) itemFile.get(name + ".recipe.permission");
                PDC.add("craftPerms;" + permission);
            }
            if (Bukkit.getRecipesFor(item).isEmpty() && isItemSet(name + ".recipe.type", itemFile)) {
                if (itemFile.get(name + ".recipe.type").toString().toLowerCase(Locale.ROOT).equals("shaped")) {
                    NamespacedKey key = new NamespacedKey(TheHordes.getPlugin(), name);
                    ShapedRecipe recipe = new ShapedRecipe(key, item);
                    List<String> shapeString = (List)itemFile.get(name + ".recipe.shape");
                    String[] shapes = shapeString.toArray(new String[shapeString.size()]);
                    recipe.shape(shapes);

                    for(Object s : (List) Objects.requireNonNull(itemFile.get(name + ".recipe.ingredients"))) {
                        String[] splitIngredients = s.toString().split(":", 2);
                        recipe.setIngredient(splitIngredients[0].charAt(0), Material.getMaterial(splitIngredients[1]));
                    }

                    Bukkit.getServer().addRecipe(recipe);
                } else {
                    NamespacedKey key = new NamespacedKey(TheHordes.getPlugin(), name);
                    ShapelessRecipe recipe = new ShapelessRecipe(key, item);

                    for(Object s : (List)itemFile.get(name + ".recipe.ingredients")) {
                        String[] splitIngredients = s.toString().split(":");
                        recipe.addIngredient(Integer.parseInt(splitIngredients[1]), Material.getMaterial(splitIngredients[0]));
                    }

                    Bukkit.getServer().addRecipe(recipe);
                }
            }

            for (String s : PDC){
                String[] values = s.split(";");
                PDCHelper.setItemPDC(values[0], item, values[1]);
            }

            return item;
        }
    }
    public boolean isItemSet(String path, FileConfiguration itemFile) {
        return itemFile.get(path) != null;
    }
    public boolean isConfigSet(String path) {
        return getFileConfig("config").get(path) != null;
    }

    public boolean itemExists(String name){
        List<Object> nodes = getAllNodesInDB("itemDB");
        for (Object o : nodes){
            if (o instanceof String s){
                if (s.equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public void changeRoot(String file, String path, String newRootName){
        if (getFileConfig(file).get(path) == null){ return; }
        Object data = getFileConfig(file).get(path);
        List<Object> options = getNodes(file, path);
        HashMap<Object, Object> values = new HashMap<>();

        for (Object o : options){
            values.put(o, getOption(file, path + "." + o));
        }

        setOption(file, path, null);

        if (newRootName != null) {
            values.forEach((k, v) -> setOption(file, newRootName + "." + k, v));
        } else{
            values.forEach((k, v) -> setOption(file, k.toString(), v));
        }
    }
}
