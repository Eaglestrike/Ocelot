package org.team114.ocelot.subsystems;

import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.modules.Lift;

public class Superstructure implements AbstractSuperstructure {
    private final Carriage carriage;
    private final Lift lift;

    private double goalHeight;

    public Superstructure(Carriage carriage, Lift lift) {
        this.carriage = carriage;
        this.lift = lift;
    }

    @Override
    public void onStart(double timestamp) {
        goalHeight = lift.getHeight();
    }

    @Override
    public void onStop(double timestamp) {
    }

    @Override
    public void onStep(double timestamp) {
        lift.goToHeight(goalHeight);
    }

    @Override
    public void incrementHeight(double increment) {
        goalHeight += increment;
    }

    @Override
    public double getHeight() {
        return lift.getHeight();
    }

    @Override
    public void actuateCarriage(boolean open) {
        carriage.actuateIntake(open);
    }

    @Override
    public void spinCarriage(boolean spin) {
        carriage.setSpin(spin);
    }

    @Override
    public void actuateCarriageLift(Carriage.LiftStage stage) {
        carriage.actuateLift(stage);
    }
}
