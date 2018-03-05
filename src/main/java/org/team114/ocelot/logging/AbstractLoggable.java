package org.team114.ocelot.logging;

import java.time.Instant;
import java.util.UUID;

/**
 * An abstract implementation of {@link Loggable}.
 */
public abstract class AbstractLoggable implements Loggable {
    private final UUID id = UUID.randomUUID();
    private final Instant timestamp = Instant.now();

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
