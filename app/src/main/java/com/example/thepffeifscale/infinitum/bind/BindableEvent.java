package com.example.thepffeifscale.infinitum.bind;

import com.example.thepffeifscale.infinitum.lua.LuaCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class BindableEvent {
    public static class Connection {
        String id;
        Consumer<HashBundle> func;
        BindableEvent event;
        boolean connected;
        public Connection(BindableEvent bindableEvent, Consumer<HashBundle> func, String id) {
            this.func = func;
            this.id = id;
            this.event = bindableEvent;
            this.connected = true;
        }

        void fire(HashBundle hashBundle) {
            this.func.accept(hashBundle);
        }

        public void disconnect() {
            this.event.disconnect(this.id);
            this.func = null;
            this.id = null;
            this.event = null;
            this.connected = false;
        }
    }

    public static class OnceConnection extends Connection {
        public OnceConnection(BindableEvent bindableEvent, Consumer<HashBundle> func, String id) {
            super(bindableEvent, func, id);
        }

        @Override
        void fire(HashBundle hashBundle) {
            super.fire(hashBundle);
            this.disconnect();
        }
    }

    private HashMap<String, Connection> connections;

    public BindableEvent() {
        connections = new HashMap<>();
    }

    public Connection connect(Consumer<HashBundle> func) {
        String uuid = UUID.randomUUID().toString();
        Connection connection = new Connection(this, func, uuid);
        this.connections.put(uuid, connection);
        return connection;
    }

    public Connection once(Consumer<HashBundle> func) {
        String uuid = UUID.randomUUID().toString();
        Connection connection = new OnceConnection(this, func, uuid);
        this.connections.put(uuid, connection);
        return connection;
    }

    public void fire(HashBundle hashBundle) {
        ArrayList<Thread> threadArrayList = new ArrayList<>();
        for (Connection connection : this.connections.values()) {
            Thread thread = new Thread(() -> connection.fire(hashBundle));
            threadArrayList.add(thread);
            thread.start();
        }

        for (Thread thread : threadArrayList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public HashBundle waitForFire() {
        AtomicBoolean hasFired = new AtomicBoolean(false);
        AtomicReference<HashBundle> returnBundle = new AtomicReference<HashBundle>();
        this.once((HashBundle hashBundle) -> {
            hasFired.set(true);
            returnBundle.set(hashBundle);
        });
        while (!hasFired.get()) {
            LuaCore.wait(1d/60d);
        }
        return returnBundle.get();
    }

    private void disconnect(String key) {
        Connection connection = this.connections.remove(key);
    }
}
