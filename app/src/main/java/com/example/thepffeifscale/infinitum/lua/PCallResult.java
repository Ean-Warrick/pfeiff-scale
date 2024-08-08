package com.example.thepffeifscale.infinitum.lua;

public class PCallResult {
    public boolean success;
    public Object result;

    public PCallResult(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }
}
