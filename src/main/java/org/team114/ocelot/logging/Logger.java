package org.team114.ocelot.logging;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Logger<T extends Loggable> {
    private final ArrayList<T> list = new ArrayList<>();

    public void log(T item) {
        list.add(item);
    }

    public List<T> getLog(){
        return list;
    }

    public List<JsonObject> dumpLogsToJson() {
        return list.stream()
                .map(T::toJson)
                .collect(toList());
    }
}
