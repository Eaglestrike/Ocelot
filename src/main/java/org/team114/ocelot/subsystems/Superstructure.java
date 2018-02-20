package org.team114.ocelot.subsystems;

import org.team114.lib.util.DashboardHandle;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.modules.Lift;
import org.team114.ocelot.settings.Settings;

public class Superstructure implements SuperstructureInterface {

    private final DashboardHandle currentHeightDB = new DashboardHandle("Current Height");
    private final DashboardHandle goalHeightDB = new DashboardHandle("Goal Height");

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
        lift.zeroEncodersIfNecessary();
        lift.goToHeight(goalHeight);

        currentHeightDB.put(lift.getHeight());
        goalHeightDB.put(goalHeight);
    }

    @Override
    public void incrementHeight(double increment) {
        setHeight(getHeight() + increment);
    }

    @Override
    public double getHeight() {
        return lift.getHeight();
    }

    @Override
    public void setHeight(double setPoint) {
        goalHeight = Math.min(Math.max(setPoint, 0), Settings.Lift.MAX_HEIGHT);
    }

    @Override
    public void actuateCarriage(boolean open) {
        carriage.actuateIntake(open);
    }

    @Override
    public void spinCarriage(double command) {
        carriage.setSpin(command);
    }

    @Override
    public void actuateCarriageLift(Carriage.ElevationStage stage) {
        carriage.actuateLift(stage);
    }
}
