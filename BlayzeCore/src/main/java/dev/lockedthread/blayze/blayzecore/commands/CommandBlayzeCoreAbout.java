package dev.lockedthread.blayze.blayzecore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@BCommand.Name(aliases = "about")
public class CommandBlayzeCoreAbout extends BCommand {

    @Override
    public void execute(CommandSender commandSender, String label, String[] arguments) {
        commandSender.sendMessage("");
        commandSender.sendMessage(ChatColor.RED + "Description: This plugin allows Blayze Developers to create plugins easier with less redundancy and more efficiency.");
        commandSender.sendMessage(ChatColor.RED + "Author: LockedThread#5691");
        commandSender.sendMessage("");
    }
}
