package org.team114.ocelot.event.example;

import org.team114.ocelot.event.Event;
import org.team114.ocelot.event.EventQueue;

public class Example {
    public static class ExEvent extends Event {}
    public static class ExQueue extends EventQueue<ExEvent> {}
    public static class PrintEvent extends ExEvent {}
    public static class OutputEvent extends ExEvent {}

    ExQueue queue = new ExQueue();

    public Example() {
        queue.add(new PrintEvent());
        queue.add(new OutputEvent());
    }

    public void step() {
        ExEvent event = queue.poll();

        if (event instanceof PrintEvent) {
            System.out.println("Print!");
        } else if (event instanceof OutputEvent) {
            System.out.println("Output.");
        }
    }
}
