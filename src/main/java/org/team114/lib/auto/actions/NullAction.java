package org.team114.lib.auto.actions;

/**
 * An action that does absolutely nothing.
 */
public class NullAction implements Action {
    @Override
    public boolean finished() {
        return true;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void step() {
    }
}
