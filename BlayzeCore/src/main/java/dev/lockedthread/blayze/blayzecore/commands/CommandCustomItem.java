package dev.lockedthread.blayze.blayzecore.commands;

import dev.lockedthread.blayze.blayzecore.commands.tabcomplete.TabCompletable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@BCommand.Name(aliases = {"customitem", "customitems", "blayzeitems", "blayzeitem"})
@BCommand.Permission(permission = "blayzecore.customitem.")
@BCommand.Description(description = "Gives players items")
public class CommandCustomItem extends BCommand implements TabCompletable {

    public CommandCustomItem() {
        addSubCommands(new CommandCustomItemGive());
    }

    @Override
    public void execute(CommandSender commandSender, String label, String[] arguments) {
        if (arguments.length == 0) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
        } else if (arguments.length == 2) {

        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String label, String[] args) {
        return TabCompletable.getTabCompletableFromSubCommands(this, commandSender, label, args);
    }
}
