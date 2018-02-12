package org.team114.ocelot.modules;

import org.team114.ocelot.util.PercentageRange;

public interface Controller {
    PercentageRange throttle();

    PercentageRange wheel();

    boolean quickTurn();

    boolean wantLowGear();

    boolean liftUp();

    boolean liftDown();

    boolean intakeSpinning();

    boolean intakeActuated();

    Carriage.ElevationStage intakeElevationStage();
}
