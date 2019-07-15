package dev.lockedthread.blayze.blayzecore.commands;

import dev.lockedthread.blayze.blayzecore.commands.tabcomplete.TabCompletable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@BCommand.Name(aliases = "blayzecore")
@BCommand.Permission(permission = "blayzecore.admin")
public class CommandBlayzeCore extends BCommand implements TabCompletable {

    public CommandBlayzeCore() {
        addSubCommands(new CommandBlayzeCoreHelp(), new CommandBlayzeCoreAbout());
    }

    @Override
    public void execute(CommandSender commandSender, String label, String[] arguments) {
        commandSender.sendMessage("");
        commandSender.sendMessage(ChatColor.RED + "Description: This plugin allows Blayze Developers to create plugins easier with less redundancy and more efficiency.");
        commandSender.sendMessage(ChatColor.RED + "Author: LockedThread#5691");
        commandSender.sendMessage("");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String label, String[] args) {
        return TabCompletable.getTabCompletableFromSubCommands(this, commandSender, label, args);
    }
}
