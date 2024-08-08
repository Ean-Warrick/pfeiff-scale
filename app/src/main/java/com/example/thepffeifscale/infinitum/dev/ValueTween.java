package com.example.thepffeifscale.infinitum.dev;

import com.example.thepffeifscale.infinitum.bind.BindableEvent;
import com.example.thepffeifscale.infinitum.lua.LuaCore;

public class ValueTween {
    static RunService runService = RunService.singleton();
    private Object goal;
    private double length;
    private TweenableValue target;
    BindableEvent finished;

    public ValueTween(TweenableValue target, Object goal, double length) {
        this.target = target;
        this.goal = goal;
        this.length = length;
        this.finished = new BindableEvent();
    }

    public void start() {
        double delta = 0;
        TweenableValue start = this.target.clone();
        while (delta < this.length) {
            this.target.tweenStep(this.goal, start, delta / this.length);
            delta += LuaCore.wait(1d/60d);
        }
        this.target.tweenStep(this.goal, start, 1);
    }

    public void stop() {

    }
}
