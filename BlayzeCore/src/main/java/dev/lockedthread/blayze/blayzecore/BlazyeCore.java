package dev.lockedthread.blayze.blayzecore;

import dev.lockedthread.blayze.blayzecore.module.Module;

public class BlazyeCore extends Module {

    private static BlazyeCore instance;

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    public static BlazyeCore getInstance() {
        return instance;
    }
}
