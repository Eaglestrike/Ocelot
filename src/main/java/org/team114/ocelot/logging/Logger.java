package org.team114.ocelot.logging;

import java.util.ArrayList;
import java.util.List;

public class Logger<T extends Loggable> {
    private ArrayList<T> list = new ArrayList<>();

    public void log(T item) {
        list.add(item);
    }

    public List<T> getLog(){
        return list;
    }
}
