package org.team114.ocelot.controllers;

import org.team114.lib.util.EdgeDetector;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.util.PercentageRange;

public class ComposedEdgeDetectingController implements Controller {

    // would make it final but it doesn't compile
    private Controller controller;
    private final EdgeDetector.EdgeType toDetect;

    public ComposedEdgeDetectingController(Controller controller, EdgeDetector.EdgeType toDetect) {
        if (controller == null) {
            throw new IllegalArgumentException("The controller cannot be null!");
        }
        this.controller = controller;
        this.toDetect = toDetect;
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
    EdgeDetector cairrageOpen = new EdgeDetector(controller::carriageOpen);
    @Override
    public boolean carriageOpen() {
        return cairrageOpen.getEdge() == toDetect;
    }

    EdgeDetector carriageIntake = new EdgeDetector(controller::carriageIntake);
    @Override
    public boolean carriageIntake() {
        return carriageIntake.getEdge() == toDetect;
    }

    EdgeDetector carriageClose = new EdgeDetector(controller::carriageClose);
    @Override
    public boolean carriageClose() {
        return carriageClose.getEdge() == toDetect;
    }

    EdgeDetector carriageOuttake = new EdgeDetector(controller::carriageOuttake);
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

    EdgeDetector lowHeight = new EdgeDetector(controller::lowHeight);
    @Override
    public boolean lowHeight() {
        return switchHeight.getEdge() == toDetect;
    }

    EdgeDetector switchHeight = new EdgeDetector(controller::switchHeight);
    @Override
    public boolean switchHeight() {
        return switchHeight.getEdge() == toDetect;
    }

    EdgeDetector scaleHeight = new EdgeDetector(controller::scaleHeight);
    @Override
    public boolean scaleHeight() {
        return scaleHeight.getEdge() == toDetect;
    }


    EdgeDetector liftZeroCalibration = new EdgeDetector(controller::liftZeroCalibration);
    @Override
    public boolean liftZeroCalibration() {
        return liftZeroCalibration.getEdge() == toDetect;
    }
}
