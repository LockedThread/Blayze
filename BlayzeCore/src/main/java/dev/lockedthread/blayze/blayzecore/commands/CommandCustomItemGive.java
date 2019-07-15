package dev.lockedthread.blayze.blayzecore.commands;

import dev.lockedthread.blayze.blayzecore.commands.tabcomplete.TabCompletable;
import dev.lockedthread.blayze.blayzecore.items.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@BCommand.Name(aliases = "give")
public class CommandCustomItemGive extends BCommand implements TabCompletable {

    @Override
    public void execute(CommandSender commandSender, String label, String[] arguments) {
        sendMessage(commandSender, "CommandCustomItemGive - commandSender = [" + commandSender + "], label = [" + label + "], arguments = [" + Arrays.toString(arguments) + "]");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String label, String[] args) {
        System.out.println("CommandCustomItemGive.onTabComplete 1 commandSender = [\" + commandSender + \"], label = [\" + label + \"], args = [\" + Arrays.toString(args) + \"]");
        if (args.length == 1) {
            System.out.println("CommandCustomItemGive.onTabComplete 2");
            if (args[0].equalsIgnoreCase("give")) {
                System.out.println("CommandCustomItemGive.onTabComplete 3");
                return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                System.out.println("CommandCustomItemGive.onTabComplete 4");
                if (CustomItem.getCustomItemMap().isEmpty()) {
                    System.out.println("CommandCustomItemGive.onTabComplete 5");
                    return null;
                } else {
                    System.out.println("CommandCustomItemGive.onTabComplete 6");
                    return new ArrayList<>(CustomItem.getCustomItemMap().keySet());
                }
            }
        }
        System.out.println("CommandCustomItemGive.onTabComplete 7");
        return null;
    }
}
