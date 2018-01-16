package org.team114.ocelot.logging;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Logger<T extends Loggable> {
    private ArrayList<T> list = new ArrayList<>();

    public void log(T item) {
        list.add(item);
    }

    public List<T> getLog(){
        return list;
    }

    public List<JsonObject> dumpLogsToJson() {
        return list.stream()
                .map(p -> p.toJson())
                .collect(Collectors.toList());
    }
}
