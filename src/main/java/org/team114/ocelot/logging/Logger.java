package org.team114.ocelot.logging;

import java.util.ArrayList;

public class Logger<T extends Loggable> {
    private ArrayList<T> list = new ArrayList<>();

    public void log(T item) {
        list.add(item);
    }
}
