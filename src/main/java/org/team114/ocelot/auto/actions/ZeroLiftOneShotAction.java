package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Superstructure;

/**
 * Tells the lift to zero.
 */
public class ZeroLiftOneShotAction extends OneShotAction {
    private Superstructure superstructure;

    /**
     * Given the superstructure, constructs an instance.
     * @param superstructure the superstructure
     */
    public ZeroLiftOneShotAction(Superstructure superstructure) {
        this.superstructure = superstructure;
    }

    /**
     * Zeros the lift.
     */
    @Override
    public void start() {
        superstructure.setWantZero();
    }
}
