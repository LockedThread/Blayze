package dev.lockedthread.blayze.blayzecore.units;

import dev.lockedthread.blayze.blayzecore.BlayzeCore;
import dev.lockedthread.blayze.blayzecore.utils.CallBack;

public abstract class Unit {

    public static final BlayzeCore BLAYZE_CORE = BlayzeCore.getInstance();
    private CallBack callBack;

    public abstract void setup();

    public void hookDisable(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }
}
