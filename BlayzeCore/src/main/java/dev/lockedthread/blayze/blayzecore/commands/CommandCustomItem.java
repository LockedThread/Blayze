package dev.lockedthread.blayze.blayzecore.commands;

import dev.lockedthread.blayze.blayzecore.commands.tabcomplete.TabCompletable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.List;
import java.util.stream.Collectors;

public class CommandCustomItem extends BCommand implements TabCompletable {
    @Override
    public void execute(CommandSender commandSender, String label, String[] arguments) {

    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("give")) {
                return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                //TODO: Implement CustomItems
                return null;
            }
        }
        return null;
    }
}
