package org.zycong.theHordes.commands;


import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.zycong.theHordes.commands.CommandRegister.CommandRegister;

import static org.zycong.theHordes.helpers.ColorUtils.colorize;


@CommandRegister.AutoRegisterer
public class discord {
    @CommandRegister.Command(
            name = "discord",
            playerOnly = true,
            permission = "theHordes.discord",
            aliases = "dc"
    )
    public boolean onCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage(MiniMessage.miniMessage().deserialize("<color:#61ff64>Click this to join the discord!</color><click:open_url:'https://discord.gg/m9v5KRHrca'></click>"));
        return true;
    }

}
