package org.team114.ocelot.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PubSub {
    public static PubSub shared = new PubSub();

    private Map<Class, List<EventHandler>> subscribers = new HashMap<>();

    // private so that only the shared one is used
    private PubSub() {}

    public <E extends Event> void subscribe(Class<E> eventClass, EventHandler<E> handler) {
        subscribers
            .getOrDefault(eventClass, new ArrayList<>())
            .add(handler);
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void publish(E event) {
        subscribers
            .getOrDefault(event.getClass(), new ArrayList<>())
            .forEach((handler) -> handler.handle(event));
    }
}
