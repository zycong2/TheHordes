package org.zycong.theHordes.helpers.yaml;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.zycong.theHordes.TheHordes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    public synchronized boolean saveData() {
        boolean ok = true;
        for (String config : TheHordes.yamlFiles) {
            cfile = new File(TheHordes.getPlugin().getDataFolder().getAbsolutePath(), config + ".yml");
            try {
                getFileConfig(config).save(cfile);
            } catch (IOException ignored) { ok = false;}
        }
        return ok;
    }

    public boolean loadData() {
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
        defaultConfig();
        return true;
    }

    public YamlConfiguration getFileConfig(String ymlFile) {
        int index = 0;
        for (String s : TheHordes.yamlFiles) {
            if (Objects.equals(s, ymlFile)) {
                return TheHordes.fileConfigurationList.get(index);
            }
            index++;
        }
        return null;
    }

    public void setOption(String file, String path, Object option){ getFileConfig(file).set(path, option); }

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
            getFileConfig("messages").options().copyDefaults(true);
        }
        return true;
    }
}
