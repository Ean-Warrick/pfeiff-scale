package com.example.thepffeifscale.infinitum.dev;

import com.example.thepffeifscale.infinitum.bind.BindableEvent;
import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.lua.LuaCore;

import java.util.HashMap;

public class EaseZ {
    static final int LINEAR = 1;
    static final int QUAD = 2;

    static final int IN = 1;
    static final int OUT = 2;

    static final int COMPLETED = 1;
    static final int STOPPED = 2;

    private static HashMap<String, Tween> tweenMap = new HashMap<>();

    public static class Tween extends Instance {
        private Tweenable target;
        private String property;
        private final double goal;
        private final double timeLength;
        public boolean isPlaying;
        public  boolean alive;
        public BindableEvent finished;

        public Tween(Tweenable target, String property, double goal, double timeLength) {
            super();
            this.target = target;
            this.property = property;
            this.goal = goal;
            this.timeLength = timeLength;
            this.isPlaying = false;
            this.alive = true;
            this.finished = new BindableEvent();
        }
        public void start() {
            if (this.alive) {
                LuaCore.spawn(() -> {
                    this.isPlaying = true;
                    double starting = this.target.getTweenValue(this.property);
                    double difference = this.goal - starting;
                    double step = (difference / this.timeLength);
                    double tick = 0;
                    while (tick < this.timeLength && difference != 0 && this.alive) {
                        tick += LuaCore.wait( 1d/60d);
                        if (tick > this.timeLength) {tick = this.timeLength;}
                        double value = (step * tick) + starting;
                        this.target.tween(this.property, value);
                    }
                    LuaCore.print(goal);
                    if (this.alive) {
                        this.target.tween(this.property, this.goal);
                        this.alive = false;
                        HashBundle bundle = new HashBundle();
                        bundle.set("state", EaseZ.COMPLETED);
                        this.finished.fire(bundle);
                    } else {
                        HashBundle bundle = new HashBundle();
                        bundle.set("state", EaseZ.STOPPED);
                        this.finished.fire(bundle);
                    }
                });
            } else {
                LuaCore.print("Tween is dead! Cannot start!");
            }
        }

        public void stop() {
            if (this.isPlaying) {
                this.isPlaying = false;
            }
        }
        @Override
        public void destroy() {
            super.destroy();
            this.target = null;
            this.property = null;
            this.finished = null;
        }
    }
    public static Tween tween(Tweenable target, String key, String property, double goal, double timeLength) {
        Tween oldTween = tweenMap.remove(key);
        if (oldTween != null) {
            oldTween.stop();
            oldTween.destroy();
        }
        Tween tween = new Tween(target, property, goal, timeLength);
        tweenMap.put(key, tween);
        return tween;
    }
}
