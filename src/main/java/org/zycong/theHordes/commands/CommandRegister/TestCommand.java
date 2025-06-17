package org.zycong.theHordes.commands.CommandRegister;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zycong.theHordes.helpers.GUI.GUI.GUI;
import org.zycong.theHordes.helpers.GUI.GUI.GUIItem;

import static org.zycong.theHordes.TheHordes.Colorize;

@CommandRegister.AutoRegisterer
public class TestCommand {

    @CommandRegister.Command(
            name = "openGUI",
            playerOnly = true,
            permission = "WhereWaldosTest.openGUI",
            root = "WhereWaldosTest"
    )
    void command(CommandSender commandSender, String[] args){
        Player player = (Player) commandSender;
        GUI gui = new GUI("&4Test GUI", GUI.Rows.ONE);
        GUIItem item = new GUIItem()
                .setName("&cClick Me!")
                .setMaterial(Material.BEACON)
                .setClickEvent(clickContext -> {
                    Player player1 = clickContext.player();
                    player1.sendMessage(Colorize(":)"));
                });
        gui.setItem(4, item);
    }

}
