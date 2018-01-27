package org.team114.lib.auto.actions;

public interface Action {
    boolean finished();
    void start();
    void stop();
    void step();
}
