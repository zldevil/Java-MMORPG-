package com.example.demoserver.event.common;

public interface EventHandler <E extends GameEvent> {

    public void handle(E gameevent);
}
