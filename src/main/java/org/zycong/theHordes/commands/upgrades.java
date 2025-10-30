package org.zycong.theHordes.commands;

public class updates implements command {
  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){ //format /th upgrades <kitname> <edit>
    Player p = (Player) CommandSender;

    switch (args[1]){
      case ("edit") : {
        openEditor(p, args[0]);
      }
    }
  }

  void openEditor(Player p, String kit){
    Inventory inv = Bukkit.CreateInventory(p, 27, "Upgrade editor of " + kit);
    List<String> upgradeOptions = (List)yamlManager.getInstance().getNodes("kits", kit + ".upgrades");
    for (String s : upgradeOptions){
      inv.addItem((ItemStack)yamlManager.getInstance().getOption("kit", kit + ".upgrades." + s + ".item"));
    }    
    p.openInventory(inv);
    p.setMetaData("inventory", new FixedMetaData(THeHordes.getPlugin(), "upgradeEditor"));
    p.setMetaData("kit", new FixedMetaData(TheHordes.getPlugin(), kit))
  }

  public void editorUsed(InventoryClickEvent e){
    if (e.getCurrentItem().getType().equals(Material.AIR)){
      return; //later add drag to add new upgrades
      e.setCanceled(true);
    }
    List<String> upgradeOptions = (List)yamlManager.getInstance().getNodes("kits", kit + ".upgrades");
    String kit = e.getWhoClicked().getMetaData("kit").get(0).asString();
    for (String s : upgradeOptions){
      if ((ItemStack)yamlManager.getInstance().getOption("kit", kit + ".upgrades." + s + ".item").equals(e.getCurrentItem())){
        createEditorForItem(e.getWhoClicked(), kit);
      }
    }
  }

  public void createEditorForItem(Player p, String kit){
    
  }
}
