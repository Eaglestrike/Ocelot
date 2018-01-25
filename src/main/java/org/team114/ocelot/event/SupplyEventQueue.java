package org.team114.ocelot.event;

import java.util.List;

public class SupplyEventQueue<T extends Event> extends EventQueue<T> {
    private List<EventQueue<? super T>> queues;

    public SupplyEventQueue(List<EventQueue<? super T>> queues) {
        this.queues = queues;
    }

    public void push(T event) {
        for (EventQueue<? super T> queue : queues) {
            queue.push(event);
        }
    }

    public T pull() {
        throw new UnsupportedOperationException("Supply queues don't support pulling.");
    }
}
