package org.team114.ocelot.logging;


import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.UUID;

public interface Loggable {

    UUID getId();
    Instant getTimestamp();
    String getTypeId();

    default JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.getId().toString());
        jsonObject.addProperty("type", getTypeId());
        jsonObject.addProperty("timestamp", this.getTimestamp().toString());
        return jsonObject;
    }

}
