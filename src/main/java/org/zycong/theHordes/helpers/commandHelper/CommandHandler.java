package org.zycong.theHordes.helpers.commandHelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandHandler {
    boolean onCommand(CommandSender commandSender, Command command, String s, String[] args);
    List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args);
}
