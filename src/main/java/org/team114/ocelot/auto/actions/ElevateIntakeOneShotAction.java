package org.team114.ocelot.auto.actions;

import org.team114.ocelot.modules.CarriageElevationStage;
import org.team114.ocelot.subsystems.Superstructure;

public class ElevateIntakeOneShotAction extends OneShotAction {

    private final CarriageElevationStage stage;
    private final Superstructure superstructure;

    public ElevateIntakeOneShotAction(Superstructure sstruct, CarriageElevationStage stage) {
        superstructure = sstruct;
        this.stage = stage;
    }

    @Override
    public void start() {
        superstructure.actuateIntakeLift(stage);
    }
}
