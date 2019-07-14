package dev.lockedthread.blayze.blayzecore.commands.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TabCompletable extends TabCompleter {

    List<String> onTabComplete(CommandSender commandSender, String label, String[] args);

    @Override
    @Nullable
    default List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return onTabComplete(commandSender, s, strings);
    }
}
