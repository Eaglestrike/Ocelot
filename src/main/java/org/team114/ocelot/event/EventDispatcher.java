package org.team114.ocelot.event;

public interface EventDispatcher<T extends Event> {
    void push(T event);
}
