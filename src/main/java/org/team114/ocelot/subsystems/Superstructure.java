package org.team114.ocelot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.util.DashboardHandle;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.modules.Lift;
import org.team114.ocelot.settings.Settings;

public class Superstructure implements SuperstructureInterface {

    private static class State {
        enum StateEnum {
            CLOSED,
            OPEN_IDLE,
            INTAKING,
            OUTTAKING,
            ZEROING
        }

        final StateEnum state;
        final double timestamp;

        State(StateEnum state, double currentTime) {
            this.timestamp = currentTime;
            this.state = state;
        }
    }

    private State state;

    private final DashboardHandle currentHeightDB = new DashboardHandle("Current Height");
    private final DashboardHandle goalHeightDB = new DashboardHandle("Goal Height");

    private final Carriage carriage;
    private final Lift lift;

    private int goalHeight;

    public Superstructure(Carriage carriage, Lift lift) {
        this.carriage = carriage;
        this.lift = lift;
    }

    @Override
    public void onStart(double timestamp) {
        goalHeight = lift.getHeight();
        state = new State(State.StateEnum.CLOSED, Timer.getFPGATimestamp());
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

        // handle intake transitions
        switch (state.state) {
            case CLOSED:
                actuateCarriage(false);
                carriage.setSpeedToProximitySensor();
                break;
            case INTAKING:
                actuateCarriage(true);
                spinCarriage(Settings.Carriage.INTAKE_IN_COMMAND);
                break;
            case OUTTAKING:
                spinCarriage(Settings.Carriage.INTAKE_OUT_COMMAND);
                if (timestamp - state.timestamp > Settings.SuperStructure.OUTTAKE_TIME_SECONDS) {
                    setState(State.StateEnum.CLOSED);
                }
                break;
            case OPEN_IDLE:
                actuateCarriage(true);
                spinCarriage(0);
                break;
            case ZEROING:
                carriage.setSpeedToProximitySensor();
                incrementHeight(Settings.SuperStructure.ZEROING_INCREMENT_TICKS);
                if (lift.zeroEncodersIfNecessary()) {
                    setHeight(0);
                }
                setState(State.StateEnum.CLOSED);
                break;
        }
    }

    private void setState(State.StateEnum state) {
        this.state = new State(state, Timer.getFPGATimestamp());
    }

    @Override
    public void incrementHeight(int increment) {
        setHeight(getHeight() + increment);
    }

    @Override
    public int getHeight() {
        return lift.getHeight();
    }

    @Override
    public void setHeight(int setPoint) {
        goalHeight = Math.min(Math.max(setPoint, 0), Settings.Lift.MAX_HEIGHT_TICKS);
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
