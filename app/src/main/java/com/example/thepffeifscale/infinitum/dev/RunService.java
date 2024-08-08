package com.example.thepffeifscale.infinitum.dev;

import com.example.thepffeifscale.infinitum.bind.BindableEvent;
import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.lua.LuaCore;

public class RunService {
    public BindableEvent stepped;
    public BindableEvent renderStepped;
    public BindableEvent ended;
    public boolean isRunning;

    private static RunService singleton = null;

    private RunService() {
        this.isRunning = false;
        this.stepped = new BindableEvent();
        this.ended = new BindableEvent();
        this.renderStepped = new BindableEvent();
        this.start();
    }

    public static RunService singleton() {
        if (RunService.singleton != null) {
            return RunService.singleton;
        } else {
            return new RunService();
        }
    }

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            LuaCore.spawn(() -> {
                while (this.isRunning) {
                    double delta = LuaCore.wait(1d/60d);
                    HashBundle bundle = new HashBundle();
                    bundle.set("delta", delta);
                    this.stepped.fire(bundle);
                }
                this.ended.fire(null);
            });
            LuaCore.spawn(() -> {
                while (this.isRunning) {
                    double delta = LuaCore.wait(1d/60d);
                    HashBundle bundle = new HashBundle();
                    bundle.set("delta", delta);
                    this.renderStepped.fire(bundle);
                }
                this.ended.fire(null);
            });
        }
    }

    public void finished() {
        while (this.isRunning) {
            LuaCore.wait((float) 1/60);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
        }
    }

}
