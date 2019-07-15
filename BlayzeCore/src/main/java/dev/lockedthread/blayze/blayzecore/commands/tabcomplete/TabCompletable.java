package dev.lockedthread.blayze.blayzecore.commands.tabcomplete;

import dev.lockedthread.blayze.blayzecore.commands.BCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface TabCompletable extends TabCompleter {

    List<String> onTabComplete(CommandSender commandSender, String label, String[] args);

    static List<String> getTabCompletableFromSubCommands(BCommand bCommand, CommandSender commandSender, String label, String[] args) {
        if (args == null) {
            return null;
        }

        Map<String, BCommand> subCommands = bCommand.getSubCommands(false);
        if (subCommands != null) {
            if (args.length == 1) {
                return new ArrayList<>(subCommands.keySet());
            }
            if (args.length > 1) {
                BCommand subCommand = subCommands.get(args[0]);
                if (subCommand.hasSubCommands()) {
                    return new ArrayList<>(subCommand.getSubCommands().keySet());
                }
                if (subCommand instanceof TabCompletable) {
                    return ((TabCompletable) subCommand).onTabComplete(commandSender, label, Arrays.copyOfRange(args, args.length - 2, args.length - 1));
                }
                return null;
            }
        }
        return null;
    }

    @Override
    @Nullable
    default List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> value = onTabComplete(commandSender, s, strings);
        return value == null ? Collections.emptyList() : value;
    }
}
