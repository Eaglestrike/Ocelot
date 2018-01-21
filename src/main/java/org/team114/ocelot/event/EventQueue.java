package org.team114.ocelot.event;


import com.google.gson.JsonObject;
import org.team114.ocelot.logging.Loggable;
import org.team114.ocelot.logging.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class EventQueue<T extends Event> {
    public final Deque<T> queue = new ArrayDeque<>();
    public final Logger<T> log = new Logger<>();

    public synchronized void push(T event) {
        queue.addLast(event);
    }

    public synchronized T pull() {
        T event = queue.pollFirst();
        log.log(event);
        return event;
    }

    public List<T> getLog(){
        return log.getLog();
    }

    public List<JsonObject> dumpLogsToJson() {
        return log.dumpLogsToJson();
    }
}
