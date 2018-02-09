package org.team114.ocelot.subsystems;

import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.modules.Lift;
import org.team114.ocelot.settings.Settings;

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
        goalHeight = Math.min(goalHeight, Settings.MAX_LIFT_HEIGHT);
        goalHeight = Math.max(goalHeight, 0);
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
    public void dropCarriage(boolean dropped) {
        carriage.actuateIntake(dropped);
    }
}
