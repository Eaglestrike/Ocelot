package org.team114.ocelot.subsystems.drive;

import org.team114.ocelot.event.Event;

public interface EventHandler {
    <E extends Event> void handle(E event);
}
