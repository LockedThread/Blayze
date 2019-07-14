package dev.lockedthread.blayze.blayzecore.commands.executor;

import dev.lockedthread.blayze.blayzecore.commands.BCommand;
import dev.lockedthread.blayze.blayzecore.module.Module;
import dev.lockedthread.blayze.blayzecore.utils.CommandMapUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BCommandExecutor implements CommandExecutor {

    public static BCommandExecutor instance;

    private final Map<String, BCommand> COMMAND_MAP = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        BCommand bCommand = COMMAND_MAP.get(label.toLowerCase());
        if (bCommand != null) {
            bCommand.perform(commandSender, label, args);
        }
        throw new RuntimeException("This shouldn't happen, report to LockedThread if you see this.");
    }

    public void loadCommand(Module module, BCommand bCommand) {
        String[] aliases = bCommand.getAliases();
        CommandMapUtil.getInstance().registerCommand(module, aliases);
        module.getCommandSet().add(bCommand);
        for (String alias : aliases) {
            COMMAND_MAP.put(alias.toLowerCase(), bCommand);
        }
    }

    public void unloadCommand(BCommand bCommand) {
        String[] aliases = bCommand.getAliases();
        CommandMapUtil.getInstance().unregisterCommand(aliases[0]);
        for (String alias : aliases) {
            COMMAND_MAP.remove(alias.toLowerCase());
        }
    }

    public void unloadModule(Module module) {
        Set<BCommand> set = module.getCommandSet(false);
        if (set != null) {
            CommandMapUtil.getInstance().unregisterCommands(module);
            for (BCommand bCommand : set) {
                unloadCommand(bCommand);
            }
        }
    }

    public Map<String, BCommand> getCommandMap() {
        return COMMAND_MAP;
    }

    public static BCommandExecutor getInstance() {
        return instance == null ? instance = new BCommandExecutor() : instance;
    }
}
