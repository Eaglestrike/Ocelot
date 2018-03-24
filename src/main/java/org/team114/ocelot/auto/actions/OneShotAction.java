package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;

/**
 * An action that happens all at once, in the start method.
 */
public abstract class OneShotAction implements Action {

    /**
     * Instances can't have any continuous behavior, so this is a no op.
     */
    @Override
    public final void stop() {}

    /**
     * Instances can't have any continuous behavior, so this is a no op.
     */
    @Override
    public final void step() {}

    /**
     * Instances must be done essentially instantaneously, with all behavior
     * confined to the start method, so by the time this is called they will be done.
     * @return always true
     */
    @Override
    public final boolean finished() {
        return true;
    }
}
