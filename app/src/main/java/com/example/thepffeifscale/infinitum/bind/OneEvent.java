package com.example.thepffeifscale.infinitum.bind;

import java.util.function.Consumer;

public class OneEvent {

    private static String ONE_ID = "ONE_EVENT_ID";
    static OneEvent singleton = null;
    private BindableEvent event;


    private OneEvent() {
        this.event = new BindableEvent();
    }

    public void fire(String id, HashBundle hashBundle) {
        hashBundle.set(ONE_ID, id);
        this.event.fire(hashBundle);
    }

    public BindableEvent.Connection connect(String id, Consumer<HashBundle> func) {
        Consumer<HashBundle> consumer = hashBundle -> {
            if(id.equals(hashBundle.getString(ONE_ID))) {
                func.accept(hashBundle);
            }
        };

        return this.event.connect(consumer);
    }

    public static OneEvent singleton() {
        if (singleton == null) {
            singleton = new OneEvent();
        }
        return singleton;
    }
}
