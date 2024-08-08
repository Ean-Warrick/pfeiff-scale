package com.example.thepffeifscale.infinitum.dev;

public class Udim2 {
    public Udim x;
    public Udim y;
    public Udim2(double xScale, double xOffset, double yScale, double yOffset) {
        this.x = new Udim(xScale, xOffset);
        this.y  = new Udim(yScale, yOffset);
    }
}
