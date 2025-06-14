package org.zycong.theHordes.helpers.GUI.GUI;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zycong.theHordes.TheHordes;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.zycong.theHordes.TheHordes.Colorize;

public class GUIItem {

    private Material material;
    private String name;
    private Integer amount;
    private List<String> lore;
    private List<Enchants> enchantments;
    private Integer customModelData;
    private Consumer<ClickContext> clickEvent;

    public ItemStack toItemStack(){
        ItemStack item = new ItemStack(this.material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Colorize(name));
        meta.lore(lore.stream()
                .map(TheHordes::Colorize)
                .toList()
        );
        enchantments.forEach(e -> {
            meta.addEnchant(e.getEnchantment(), e.getLevel(), e.getBoo());
        });
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        item.setAmount(amount);

        return item;
    }




    public Material getMaterial() {return material;}

    public GUIItem setMaterial(Material material) {this.material = material;return this;}

    public String getName() {return name;}

    public GUIItem setName(String name) {this.name = name;return this;}

    public List<String> getLore() {return lore;}

    public GUIItem setLore(List<String> lore) {this.lore = lore;return this;}

    public List<Enchants> getEnchantments() {return enchantments;}
    public GUIItem setEnchantments(List<Enchants> enchantments) {this.enchantments = enchantments;return this;}

    public Integer getCustomModelData() {return customModelData;}

    public GUIItem setCustomModelData(Integer customModelData) {this.customModelData = customModelData;return this;}

    public Consumer<ClickContext> getClickEvent() {return clickEvent;}

    public GUIItem setClickEvent(Consumer<ClickContext> clickEvent) {this.clickEvent = clickEvent;return this;}





    public record ClickContext(Player player, ClickType clickType, GUI gui) {}
}
