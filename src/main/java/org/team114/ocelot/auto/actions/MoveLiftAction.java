package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Superstructure;

public class MoveLiftAction implements Action {
    protected final Superstructure superstructure;
    protected final int ticks;

    /**
     * Creates an action to set the lift to a given height in ticks.
     * @param superstructure the superstructure
     * @param ticks requested height in ticks
     */
    public MoveLiftAction(Superstructure superstructure, int ticks) {
        this.superstructure = superstructure;
        this.ticks = ticks;
    }

    @Override
    public boolean finished() {
        return Epsilon.epsilonEquals(superstructure.getHeight(), ticks, Settings.SuperStructure.COMPLETE_ACTION_THRESHOLD_TICKS);
    }

    /**
     * Sets the lift to the requested height.
     */
    @Override
    public void start() {
        superstructure.setHeight(ticks);
    }

    @Override
    public void stop() {
    }

    @Override
    public void step() {

    }
}
