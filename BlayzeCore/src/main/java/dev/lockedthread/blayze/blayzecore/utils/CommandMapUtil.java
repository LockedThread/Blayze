package dev.lockedthread.blayze.blayzecore.utils;

import dev.lockedthread.blayze.blayzecore.commands.BCommand;
import dev.lockedthread.blayze.blayzecore.commands.executor.BCommandExecutor;
import dev.lockedthread.blayze.blayzecore.commands.tabcomplete.TabCompletable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class CommandMapUtil {

    private static CommandMapUtil instance;

    private static Field commandMap;
    private static Constructor<PluginCommand> commandConstructor;

    public static CommandMapUtil getInstance() {
        return instance == null ? instance = new CommandMapUtil() : instance;
    }

    private CommandMapUtil() {
        try {
            (commandMap = SimplePluginManager.class.getDeclaredField("commandMap")).setAccessible(true);
            (commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class)).setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void unregisterCommand(String s) {
        getCommandMap().getCommand(s).unregister(getCommandMap());
    }

    public void unregisterCommands(Plugin plugin) {
        final CommandMap commandMap = getCommandMap();
        for (Command command : getCommandMap().getKnownCommands().values()) {
            PluginCommand pluginCommand = Bukkit.getPluginCommand(command.getName());
            if (pluginCommand != null && pluginCommand.getPlugin().getName().equals(plugin.getName())) {
                command.unregister(commandMap);
            }
        }
    }

    public <T extends BCommand> void registerCommand(Plugin plugin, T bCommand) {
        PluginCommand pluginCommand = getPluginCommand(plugin, bCommand);
        if (bCommand instanceof TabCompletable) {
            pluginCommand.setTabCompleter((TabCompleter) bCommand);
        }
        getCommandMap().register(plugin.getDescription().getName(), pluginCommand);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Command> getCommands() {
        try {
            return (Map<String, Command>) commandMap.get(Bukkit.getPluginManager());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to get the Bukkit KnowmCommand Map. Please contact LockedThread.", e);
        }
    }

    private CommandMap getCommandMap() {
        try {
            return (CommandMap) commandMap.get(Bukkit.getPluginManager());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to get the Bukkit CommandMap. Please contact LockedThread.", e);
        }
    }

    private PluginCommand getPluginCommand(Plugin plugin, BCommand bCommand) {
        try {
            String[] aliases = bCommand.getAliases();
            PluginCommand pluginCommand = commandConstructor.newInstance(aliases[0], plugin);
            pluginCommand.setAliases(Arrays.asList(Arrays.copyOfRange(aliases, 1, aliases.length)));
            pluginCommand.setExecutor(BCommandExecutor.getInstance());
            return pluginCommand;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Unable to get PluginCommand. Please contact LockedThread.", e);
        }
    }
}
