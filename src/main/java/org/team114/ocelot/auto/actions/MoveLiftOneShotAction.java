package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Superstructure;

/**
 * Tells the lift to move to a requested height.
 */
public class MoveLiftOneShotAction extends OneShotAction {
    private Superstructure superstructure;
    private int ticks;

    /**
     * Creates an action to set the lift to a given height in ticks.
     * @param superstructure the superstructure
     * @param ticks requested height in ticks
     */
    public MoveLiftOneShotAction(Superstructure superstructure, int ticks) {
        this.superstructure = superstructure;
        this.ticks = ticks;
    }

    /**
     * Sets the lift to the requested height.
     */
    @Override
    public void start() {
        superstructure.setHeight(ticks);
    }
}
