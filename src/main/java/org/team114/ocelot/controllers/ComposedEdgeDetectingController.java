package org.team114.ocelot.controllers;

import org.team114.lib.util.EdgeDetector;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.util.PercentageRange;

public class ComposedEdgeDetectingController implements Controller {

    // would make it final but it doesn't compile
    private final Controller controller;
    private final EdgeDetector.EdgeType toDetect;

    private EdgeDetector cairrageOpen;
    private EdgeDetector carriageIntake;
    private EdgeDetector carriageClose;
    private EdgeDetector carriageOuttake;
    private EdgeDetector lowHeight;
    private EdgeDetector switchHeight;
    private EdgeDetector scaleHeight;
    private EdgeDetector liftZeroCalibration;

    public ComposedEdgeDetectingController(Controller controller, EdgeDetector.EdgeType toDetect) {
        if (controller == null) {
            throw new IllegalArgumentException("The controller cannot be null!");
        }
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
