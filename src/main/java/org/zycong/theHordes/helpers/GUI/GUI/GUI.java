package org.zycong.theHordes.helpers.GUI.GUI;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

import static org.zycong.theHordes.TheHordes.Colorize;

public class GUI {

    private String Title;
    private static Rows Rows;
    private Inventory inventory;


    public GUI(String title, Rows rows) {
        this.Title = title;
        this.Rows = rows;

        buildInventory(this.Title, this.Rows);
    }

    public GUI(String title) {
        this.Title = title;
        this.Rows = GUI.Rows.ONE;

        buildInventory(this.Title, this.Rows);
    }

    public static void setItem(int slot, GUIItem item){

    }

    public static void setItem(int slot, ItemStack item){

    }

    public static void open(Player player){

    }

    public static void open(Integer page, Player player){

    }

    public static void open(Player player, Integer delay){

    }

    public static void open(Player player, Integer delay, Integer page){

    }

    private void buildInventory(String Title, Rows Rows){
        TextComponent colorized = Colorize(Title);
        Inventory inv = Bukkit.createInventory(null, Rows.getValue()*9, colorized);

        this.inventory = inv;
    }


    public enum Rows{
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6);

        private Integer value;

        Rows(Integer i){
            this.value = i;
        }

        Integer getValue(){
            return value;
        }
    }
}
