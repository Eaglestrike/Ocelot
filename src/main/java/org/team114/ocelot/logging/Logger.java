package org.team114.ocelot.logging;

import java.util.ArrayList;
import java.util.List;

public class Logger<T extends Loggable> {
    private List<T> list = new ArrayList<>();

    public synchronized void log(T item) {
        list.add(item);
    }
}
