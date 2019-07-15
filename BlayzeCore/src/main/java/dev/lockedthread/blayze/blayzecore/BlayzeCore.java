package dev.lockedthread.blayze.blayzecore;

import dev.lockedthread.blayze.blayzecore.commands.CommandBlayzeCore;
import dev.lockedthread.blayze.blayzecore.commands.CommandCustomItem;
import dev.lockedthread.blayze.blayzecore.items.UnitCustomItem;
import dev.lockedthread.blayze.blayzecore.module.Module;

public class BlayzeCore extends Module {

    private static BlayzeCore instance;

    @Override
    public void enable() {
        instance = this;
        registerCommands(new CommandCustomItem(), new CommandBlayzeCore());
        registerUnits(new UnitCustomItem());
    }

    @Override
    public void disable() {
        instance = null;
    }

    public static BlayzeCore getInstance() {
        return instance;
    }
}
