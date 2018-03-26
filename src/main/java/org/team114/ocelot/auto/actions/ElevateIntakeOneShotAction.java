package org.team114.ocelot.auto.actions;

import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.subsystems.Superstructure;

public class ElevateIntakeOneShotAction extends OneShotAction {

    public final Carriage.ElevationStage stage;
    private final Superstructure superstructure;

    public ElevateIntakeOneShotAction(Superstructure sstruct, Carriage.ElevationStage stage) {
        superstructure = sstruct;
        this.stage = stage;
    }

    @Override
    public void start() {
        superstructure.actuateIntakeLift(stage);
    }
}
