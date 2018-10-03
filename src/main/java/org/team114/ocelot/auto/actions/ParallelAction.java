package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;

public class ParallelAction implements Action {
    Action a;
    Action b;

    public ParallelAction(Action a, Action b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean finished() {
        return a.finished() && b.finished();
    }

    @Override
    public void start() {
        a.start();
        b.start();
    }

    @Override
    public void stop() {
        a.stop();
        b.stop();
    }

    @Override
    public void step() {
        a.step();
        b.step();
    }
}
