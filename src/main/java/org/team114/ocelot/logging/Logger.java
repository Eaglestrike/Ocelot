package org.team114.ocelot.logging;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Logs a particlar type of {@link Loggable}.
 * @param <T> type of Loggable to log
 */
public class Logger<T extends Loggable> {
    private final ArrayList<T> list = new ArrayList<>();

    /**
     * Logs an item.
     * @param item type to be logged
     */
    public void log(T item) {
        list.add(item);
    }

    /**
     * Returns the list of log items.
     * @return an immutable view of the log list
     */
    public List<T> getLog(){
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns all logs in a JSON format.
     * @return list of log items.
     */
    public List<JsonObject> dumpLogsToJson() {
        return list.stream()
                .map(T::toJson)
                .collect(toList());
    }
}
