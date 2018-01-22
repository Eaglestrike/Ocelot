package org.team114.ocelot.event;

import com.google.gson.JsonObject;
import org.team114.ocelot.logging.Loggable;

public interface Event extends Loggable {
    default JsonObject toJson() {
        return Loggable.super.toJson();
    }
}
