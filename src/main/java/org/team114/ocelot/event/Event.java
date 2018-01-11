package org.team114.ocelot.event;

import org.team114.ocelot.logging.Loggable;

import java.util.UUID;

public abstract class Event implements Loggable {
    public UUID id = UUID.randomUUID();
    public double timestamp = System.currentTimeMillis();

    public UUID getId() {
        return id;
    }

    public double getTimestamp() {
        return timestamp;
    }
}
