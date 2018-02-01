package org.team114.ocelot.logging;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractLoggable implements Loggable {
    public UUID id = UUID.randomUUID();
    public Instant timestamp = Instant.now();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getTypeId() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
