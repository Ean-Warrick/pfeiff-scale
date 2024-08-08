package com.example.thepffeifscale.infinitum.dev;

import com.example.thepffeifscale.infinitum.bind.*;
import com.example.thepffeifscale.infinitum.lua.*;

import java.util.HashMap;
import java.util.function.Consumer;

public class Instance {
    private HashMap<String, BindableEvent.Connection> connectionHashMap;

    public Instance() {
        this.connectionHashMap = new HashMap<>();
    }

    public void connect(BindableEvent event, String key, Consumer<HashBundle> func) {
        this.disconnect(key);
        this.connectionHashMap.put(key, event.connect(func));
    }

    public void disconnect(String key) {
        BindableEvent.Connection connection = this.connectionHashMap.remove(key);
        if (connection != null) {
            connection.disconnect();
        }
    }

    public void destroy() {
        LuaCore.forKVInPairs(this.connectionHashMap, (KeyValuePair keyValuePair) -> {
            String key = keyValuePair.key.toString();
            this.disconnect(key);
        });
    }
}
