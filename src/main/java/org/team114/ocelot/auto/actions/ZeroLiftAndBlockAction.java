package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Superstructure;

public class ZeroLiftAndBlockAction implements Action {
    /**
     * Given the superstructure, constructs an instance.
     * @param superstructure the superstructure
     */

    Superstructure superstructure;
    public ZeroLiftAndBlockAction(Superstructure superstructure) {
        this.superstructure = superstructure;
    }

    @Override
    public boolean finished() {
        return !superstructure.isZeroing();
    }

    @Override
    public void stop() {

    }

    @Override
    public void step() {

    }

    @Override
    public void start() {
        superstructure.setWantZero();
    }
}
