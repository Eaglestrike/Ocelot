package org.team114.ocelot.controllers;

/**
 * Translates requests for functional control input into bindings.
 */
public interface Controller {
    // ====== DRIVER ======

    double throttle();

    double wheel();

    boolean wantQuickTurn();

    boolean wantLowGear();

    // ====== OPERATOR ======

    // carriage states
    boolean carriageOpenLowSpeed();

    boolean carriageOpenIdle();
    
    boolean carriageIntake();

    boolean carriageClose();

    boolean carriageOuttake();

    boolean cairrageUp();

    boolean cairrageMiddle();

    boolean cairrageDown();

    // lift height
    double liftHeightSetPoint();

    boolean wantManualLiftHeight();

    boolean manualLiftUp();

    boolean manualLiftDown();

    boolean speedFaster();

    boolean speedSlower();

    boolean liftZeroCalibration();
}
