package com.example.thepffeifscale.infinitum.dev;

public class Vector2 implements TweenableValue {
    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void tween(Object setValue) {
        Vector2 vector2Goal = (Vector2) setValue;

    }

    @Override
    public void tweenStep(Object goal, Object start, double step) {

    }

    public TweenableValue clone() {
        return new Vector2(this.x, this.y);
    }
}
