package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Superstructure;

public class ZeroLiftAction extends MoveLiftAction {
    /**
     * Given the superstructure, constructs an instance.
     * @param superstructure the superstructure
     */
    public ZeroLiftAction(Superstructure superstructure) {
        super(superstructure, 0);
    }

    @Override
    public void start() {
        superstructure.setWantZero();
    }
}