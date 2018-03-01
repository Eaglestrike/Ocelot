package org.team114.ocelot.controllers;

import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.util.PercentageRange;

public interface Controller {
    // ====== DRIVER ======

    PercentageRange throttle();

    PercentageRange wheel();

    boolean quickTurn();

    boolean wantLowGear();

    // ====== OPERATOR ======

    // carriage states
    boolean carriageOpen();

    boolean carriageIntake();

    boolean carriageClose();

    boolean carriageOuttake();

    Carriage.ElevationStage intakeElevation();

    // lift height
    double liftIncrement();

    boolean lowHeight();

    boolean switchHeight();

    boolean scaleHeight();

    boolean liftZeroCalibration();
}
