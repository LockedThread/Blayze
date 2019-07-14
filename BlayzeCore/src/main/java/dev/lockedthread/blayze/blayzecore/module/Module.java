package dev.lockedthread.blayze.blayzecore.module;

import dev.lockedthread.blayze.blayzecore.commands.BCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public abstract class Module extends JavaPlugin {

    private Set<BCommand> commandSet;

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public abstract void enable();

    public abstract void disable();

    @Nullable
    public Set<BCommand> getCommandSet(boolean nullityRegister) {
        return commandSet == null ? nullityRegister ? (commandSet = new HashSet<>()) : null : commandSet;
    }

    @NotNull
    public Set<BCommand> getCommandSet() {
        //noinspection ConstantConditions
        return getCommandSet(true);
    }
}
