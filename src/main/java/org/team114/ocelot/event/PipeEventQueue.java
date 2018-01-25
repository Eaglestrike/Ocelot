package org.team114.ocelot.event;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PipeEventQueue<I extends Event, O extends Event> extends EventQueue<I> {
    private Predicate<I> acceptFilter;
    private Function<I, O> translator;
    private EventQueue<? super O> output;

    public PipeEventQueue(Predicate<I> acceptFilter, Function<I, O> translator,
                          EventQueue<? super O> output) {
        this.acceptFilter = acceptFilter;
        this.translator = translator;
        this.output = output;
    }

    @Override
    public void push(I event) {
        Stream.of(event)
                .filter(acceptFilter)
                .map(translator)
                .forEach(output::push);
    }
    @Override
    public I pull() {
        throw new UnsupportedOperationException("Pipe queues don't support pulling.");
    }
}
