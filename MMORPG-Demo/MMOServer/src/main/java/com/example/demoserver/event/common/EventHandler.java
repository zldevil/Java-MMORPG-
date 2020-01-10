package com.example.demoserver.event.common;

@FunctionalInterface
public interface EventHandler <E extends GameEvent> {

    public void handle(E gameEvent);

}
