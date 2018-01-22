package org.team114.ocelot.event;

import org.team114.ocelot.logging.Loggable;

import java.util.UUID;

public abstract class Event implements Loggable {
    private final UUID id = UUID.randomUUID();
    private final long timestamp = System.currentTimeMillis();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
