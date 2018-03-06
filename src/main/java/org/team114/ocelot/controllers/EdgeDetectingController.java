package org.team114.ocelot.controllers;

import org.team114.lib.util.EdgeDetector;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.util.PercentageRange;

import java.util.Objects;

/**
 * Wraps a controller with {@link EdgeDetector EdgeDetectors} to check
 * for only changes in inputs.
 */
public class EdgeDetectingController implements Controller {

    private final Controller controller;
    private final EdgeDetector.EdgeType toDetect;

    private final EdgeDetector cairrageOpen;
    private final EdgeDetector carriageIntake;
    private final EdgeDetector carriageClose;
    private final EdgeDetector carriageOuttake;
    private final EdgeDetector lowHeight;
    private final EdgeDetector switchHeight;
    private final EdgeDetector scaleHeight;
    private final EdgeDetector liftZeroCalibration;

    /**
     * Constructs an instance from a controller and a detected edge type.
     * @param controller The controller to wrap.
     * @param toDetect The type of edge to be detected. This will usually be either RISING
     *                 or FALLING.
     */
    public EdgeDetectingController(Controller controller, EdgeDetector.EdgeType toDetect) {
        Objects.requireNonNull(controller, "The controller cannot be null!");

        this.controller = controller;
        this.toDetect = toDetect;

        cairrageOpen = new EdgeDetector(controller::carriageOpen);
        carriageIntake = new EdgeDetector(controller::carriageIntake);
        carriageClose = new EdgeDetector(controller::carriageClose);
        carriageOuttake = new EdgeDetector(controller::carriageOuttake);
        lowHeight = new EdgeDetector(controller::lowHeight);
        switchHeight = new EdgeDetector(controller::switchHeight);
        scaleHeight = new EdgeDetector(controller::scaleHeight);
        liftZeroCalibration = new EdgeDetector(controller::liftZeroCalibration);

    }

    @Override
    public PercentageRange throttle() {
        return controller.throttle();
    }

    @Override
    public PercentageRange wheel() {
        return controller.wheel();
    }

    @Override
    public boolean quickTurn() {
        return controller.quickTurn();
    }

    @Override
    public boolean wantLowGear() {
        return controller.wantLowGear();
    }

    // ====== OPERATOR ======

    // carriage states
    @Override
    public boolean carriageOpen() {
        return cairrageOpen.getEdge() == toDetect;
    }

    @Override
    public boolean carriageIntake() {
        return carriageIntake.getEdge() == toDetect;
    }

    @Override
    public boolean carriageClose() {
        return carriageClose.getEdge() == toDetect;
    }

    @Override
    public boolean carriageOuttake() {
        return carriageOuttake.getEdge() == toDetect;
    }

    @Override
    public Carriage.ElevationStage intakeElevation() {
        return controller.intakeElevation();
    }

    // lift height
    @Override
    public double liftIncrement() {
        return controller.liftIncrement();
    }

    @Override
    public boolean lowHeight() {
        return lowHeight.getEdge() == toDetect;
    }

    @Override
    public boolean switchHeight() {
        return switchHeight.getEdge() == toDetect;
    }

    @Override
    public boolean scaleHeight() {
        return scaleHeight.getEdge() == toDetect;
    }


    @Override
    public boolean liftZeroCalibration() {
        return liftZeroCalibration.getEdge() == toDetect;
    }
}
