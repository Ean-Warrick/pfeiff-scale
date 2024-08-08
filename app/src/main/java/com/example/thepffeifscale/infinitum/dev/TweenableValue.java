package com.example.thepffeifscale.infinitum.dev;

public interface TweenableValue {
    void tween(Object setValue);
    void tweenStep(Object goal, Object start, double step);

    TweenableValue clone();
}
