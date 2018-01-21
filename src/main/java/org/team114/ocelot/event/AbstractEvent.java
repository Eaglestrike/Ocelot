package org.team114.ocelot.event;

import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractEvent implements Event {
    private final UUID id = UUID.randomUUID();
    private final Instant timestamp = Instant.now();
    private final String typeId;

    protected AbstractEvent(String typeId) {
        this.typeId = typeId;
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
       return Event.super.toJson();
    }
}
