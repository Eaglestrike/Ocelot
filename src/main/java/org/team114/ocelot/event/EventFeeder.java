package org.team114.ocelot.event;

import java.util.List;

public class EventFeeder<T extends Event> implements EventDispatcher<T> {
    private List<EventQueue<? super T>> queues;

    public EventFeeder(List<EventQueue<? super T>> queues) {
        this.queues = queues;
    }

    public void push(T event) {
        for (EventQueue<? super T> queue : queues) {
            queue.push(event);
        }
    }
}
