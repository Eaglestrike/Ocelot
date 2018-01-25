package org.team114.ocelot.event;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EventPipe<I extends Event, O extends Event> implements EventDispatcher<I> {
    private Predicate<I> acceptFilter;
    private Function<I, O> translator;
    private EventQueue<? super O> output;

    public EventPipe(Predicate<I> acceptFilter, Function<I, O> translator,
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
}
