package org.zycong.theHordes.commands;

public class updates implements command {
  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){ //format /th upgrades <kitname> <edit>
    Player p = (Player) CommandSender;

    switch (args[1]){
      case ("edit") : {
        createEditor(p, args[0]);
      }
    }
  }

  void createEditor(Player p, String kit){
    Inventory inv = Bukkit.CreateInventory(p, 27, "Upgrade editor of " + kit);

    
    

    p.openInventory(inv);
  }
}
