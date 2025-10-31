package org.zycong.theHordes.commands;

import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.zycong.theHordes.TheHordes;
import org.zycong.theHordes.helpers.commandHelper.CommandHandler;
import org.zycong.theHordes.helpers.yaml.yamlManager;

import java.util.List;

public class upgrades implements CommandHandler {
  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){ //format /th upgrades <kitname> <edit>
    Player p = (Player) commandSender;

    switch (args[1]){
      case ("edit") : {
        openEditor(p, args[0]);
      }
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
    return List.of();
  }

  void openEditor(Player p, String kit){
    Inventory inv = Bukkit.createInventory(p, 27, "Upgrade editor of " + kit);
    List<String> upgradeOptions = (List) yamlManager.getInstance().getNodes("kits", kit + ".upgrades");
    for (String s : upgradeOptions){
      inv.addItem((ItemStack)yamlManager.getInstance().getOption("kit", kit + ".upgrades." + s + ".item"));
    }    
    p.openInventory(inv);
    p.setMetadata("inventory", new FixedMetadataValue(TheHordes.getPlugin(), "upgradeEditor"));
    p.setMetadata("kit", new FixedMetadataValue(TheHordes.getPlugin(), kit));
  }

  public static void editorUsed(InventoryClickEvent e){
    if (e.getCurrentItem().getType().equals(Material.AIR)){
      e.setCancelled(true);
      return; //later add drag to add new upgrades

    }
    String kit = e.getWhoClicked().getMetadata("kit").get(0).asString();
    List<String> upgradeOptions = (List)yamlManager.getInstance().getNodes("kits", kit + ".upgrades");
    for (String s : upgradeOptions){
      if ((ItemStack)yamlManager.getInstance().getOption("kit", kit + ".upgrades." + s + ".item") == e.getCurrentItem()){
        createEditorForItem((Player) e.getWhoClicked(), kit);
      }
    }
  }

  public static void createEditorForItem(Player p, String kit){
    
  }
}
