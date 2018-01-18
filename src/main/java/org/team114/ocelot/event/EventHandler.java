package org.team114.ocelot.event;

import org.team114.ocelot.event.Event;

public interface EventHandler<E extends Event> {
    void handle(E event);
}
