package me.sjnez.renosense.event.events;

import me.sjnez.renosense.event.EventStage;

public class KeyEvent
extends EventStage {
    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
