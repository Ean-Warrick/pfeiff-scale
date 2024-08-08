package com.example.thepffeifscale.infinitum.lua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LuaCore {
    public static void print(Object ...objects) {
        for (Object nextObject : objects) {
            System.out.print(nextObject.toString());
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    public static void println(Object ...objects) {
        for (Object nextObject : objects) {
            System.out.print(nextObject.toString());
            System.out.println();
        }
    }

    public static double tick() {
        return System.currentTimeMillis() / 1000d;
    }

    public static Thread spawn(Runnable runnable) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                runnable.run();
            }
        };
        thread.start();
        return thread ;
    }

    public static double wait(double seconds) {
        double start = tick();
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException ignored) {}
        double end = tick();
        double delta = (end - start);
        return delta;
    }

    public static PCallResult pcall(Supplier<Object> supplier) {
        try {
            Object result = supplier.get();
            return new PCallResult(true, result);
        } catch (Exception exception){
            return new PCallResult(false, null);
        }
    }

    public static PCallResult pcall(Runnable runnable) {
        try {
            runnable.run();
            return new PCallResult(true, null);
        } catch (Error error){
            return new PCallResult(false, null);
        }
    }

    public static void forKVInPairs(HashMap hashMap, Consumer<KeyValuePair> consumer) {
        for (Object key : hashMap.keySet()) {
            consumer.accept(new KeyValuePair(key, hashMap.get(key)));
        }
    }
    public static void forKVInIPairs(ArrayList arrayList, Consumer<KeyValuePair> consumer) {
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                consumer.accept(new KeyValuePair(i, arrayList.get(i)));
            }
        }
    }

    public static void forKVInIPairs(Object[] objects, Consumer<KeyValuePair> consumer) {
        if (objects.length > 0) {
            for (int i = 0; i < objects.length; i++) {
                consumer.accept(new KeyValuePair(i, objects[i]));
            }
        }
    }
}
