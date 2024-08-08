package com.example.thepffeifscale.infinitum.bind;

import java.util.function.Function;

public class InvokableFunction {

    Function<HashBundle, HashBundle> invokeable;
    boolean isInitialized;

    public InvokableFunction() {
        this.isInitialized = false;
    }

    public void setFunc(Function<HashBundle, HashBundle> func) {
        this.invokeable = func;
        this.isInitialized = true;
    }

    public HashBundle invoke(HashBundle hashBundle) {
        if (this.isInitialized) {
            return this.invokeable.apply(hashBundle);
        } else {
            return null;
        }
    }
}
