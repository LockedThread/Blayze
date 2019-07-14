package dev.lockedthread.blayze.blayzecore.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
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
        for (Command command : getCommands().values()) {
            PluginCommand pluginCommand = Bukkit.getPluginCommand(command.getName());
            if (pluginCommand != null && pluginCommand.getPlugin().getName().equals(plugin.getName())) {
                command.unregister(commandMap);
            }
        }
    }

    public void registerCommand(Plugin plugin, String... aliases) {
        getCommandMap().register(plugin.getDescription().getName(), getPluginCommand(plugin, aliases));
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

    private PluginCommand getPluginCommand(Plugin plugin, String... aliases) {
        try {
            PluginCommand pluginCommand = commandConstructor.newInstance(aliases[0], plugin);
            pluginCommand.setAliases(Arrays.asList(Arrays.copyOfRange(aliases, 1, aliases.length)));
            return pluginCommand;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Unable to get PluginCommand. Please contact LockedThread.", e);
        }
    }
}
