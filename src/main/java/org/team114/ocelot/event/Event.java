package org.team114.ocelot.event;

import com.google.gson.JsonObject;
import org.team114.ocelot.logging.Loggable;

import java.time.Instant;
import java.util.UUID;

public abstract class Event implements Loggable {
    private final UUID id = UUID.randomUUID();
    private final Instant timestamp = Instant.now();
    private final String typeId;

    protected Event(String typeId) {
        this.typeId = "event." + typeId;
        if (typeId == null) {
            throw new NullPointerException("Type ID must be non-null.");
        }
    }

    @Override
    public final UUID getId() {
        return id;
    }
    
    @Override
    public final Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public final String getTypeId() {
        return typeId;
    }

    public JsonObject toJson() {
       return Loggable.super.toJson();
    }
}
