package org.team114.ocelot.logging;


import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.UUID;

public interface Loggable {

    UUID getId();
    Instant getTimestamp();
    String getTypeId();
    JsonObject toJson();

}
