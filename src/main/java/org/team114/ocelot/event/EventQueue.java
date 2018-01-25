package org.team114.ocelot.event;

import com.google.gson.JsonObject;
import org.team114.ocelot.logging.Logger;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class EventQueue<T extends Event> implements Iterable<T> {
    private final Deque<T> queue = new ConcurrentLinkedDeque<>();
    protected final Logger<T> log = new Logger<>();

    public synchronized void push(T event) {
        queue.addLast(event);
    }

    /**
     * Pulls from the event queue.&nbsp;Optional method.
     * @return the first item in the queue.
     * @throws UnsupportedOperationException if the implementation does not support this method
     */
    public synchronized T pull() {
        T event = queue.pollFirst();
        log.log(event);
        return event;
    }

    private class EventIterator implements Iterator<T> {
        Iterator<T> iterator = queue.iterator();
        @Override
        public T next() {
            T item = iterator.next();
            log.log(item);
            iterator.remove();
            return item;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
    }

    /**
     * Returns a new iterator, which <b>removes</b> items from the queue
     * automatically.
     * @return an iterator for this queue
     */
    @Override
    public Iterator<T> iterator() {
        return new EventIterator();
    }

    public List<T> getLog(){
        return log.getLog();
    }

    public List<JsonObject> dumpLogsToJson() {
        return log.dumpLogsToJson();
    }
}
