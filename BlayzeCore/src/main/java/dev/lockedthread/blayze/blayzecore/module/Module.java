package dev.lockedthread.blayze.blayzecore.module;

import com.google.common.collect.ImmutableSet;
import dev.lockedthread.blayze.blayzecore.commands.BCommand;
import dev.lockedthread.blayze.blayzecore.commands.executor.BCommandExecutor;
import dev.lockedthread.blayze.blayzecore.events.EventPost;
import dev.lockedthread.blayze.blayzecore.units.Unit;
import dev.lockedthread.blayze.blayzecore.utils.CommandMapUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Module extends JavaPlugin {

    private static final List<Module> ENABLED_MODULES = new ArrayList<>();

    private Set<BCommand> commandSet;
    private Set<EventPost<?>> eventPosts;
    private ImmutableSet<Unit> units;

    public abstract void enable();

    public abstract void disable();

    @Override
    public void onEnable() {
        ENABLED_MODULES.add(this);
        enable();
    }

    @Override
    public void onDisable() {
        disable();
        if (commandSet != null) {
            CommandMapUtil.getInstance().unregisterCommands(this);
            for (BCommand bCommand : commandSet) {
                bCommand.getSubCommands().clear();
            }
            commandSet.clear();
        }
        if (units != null) {
            for (Unit unit : units) {
                if (unit.getCallBack() != null) {
                    unit.getCallBack().call();
                }
            }
            this.units = null;
        }
        ENABLED_MODULES.remove(this);
    }

    public void registerCommands(BCommand... bCommands) {
        for (BCommand bCommand : bCommands) {
            BCommandExecutor.getInstance().loadCommand(this, bCommand);
        }
    }

    @Nullable
    public Set<BCommand> getCommandSet(boolean nullityRegister) {
        return commandSet == null ? nullityRegister ? commandSet = new HashSet<>() : null : commandSet;
    }

    @NotNull
    public Set<BCommand> getCommandSet() {
        //noinspection ConstantConditions
        return getCommandSet(true);
    }

    /**
     * May only be executed once per cycle
     */
    public void registerUnits(Unit... units) {
        for (Unit unit : units) {
            unit.setup();
        }
        this.units = ImmutableSet.copyOf(units);
    }

    @NotNull
    public Set<EventPost<?>> getEventPosts() {
        return eventPosts == null ? eventPosts = new HashSet<>() : eventPosts;
    }

    public static List<Module> getEnabledModules() {
        return ENABLED_MODULES;
    }
}
