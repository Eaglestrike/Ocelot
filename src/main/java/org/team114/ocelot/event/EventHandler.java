package org.team114.ocelot.event;

public interface EventHandler<E extends Event> {
    void handle(E event);
}
