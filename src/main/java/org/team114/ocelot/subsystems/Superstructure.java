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
    private final DashboardHandle distanceSensorDB = new DashboardHandle("Distance To 'Box'");


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
        lift.goToHeight(goalHeight);

        currentHeightDB.put(lift.getHeight());
        goalHeightDB.put(goalHeight);
        distanceSensorDB.put(carriage.getDistance());

        // handle intake transitions
        switch (state.state) {
            case CLOSED:
                actuateCarriage(false);
                carriage.setSpeedToProximitySensor();
                break;
            case INTAKING:
                actuateIntakeLift(Carriage.ElevationStage.LOWERED);
                actuateCarriage(true);
                spinCarriage(Settings.Carriage.INTAKE_IN_COMMAND);
                break;
            case OUTTAKING:
                spinCarriage(Settings.Carriage.INTAKE_OUT_COMMAND);
                System.out.println(timestamp + " : " + state.timestamp);
                if (timestamp - state.timestamp > Settings.SuperStructure.OUTTAKE_TIME_SECONDS) {
                    setState(State.StateEnum.CLOSED, timestamp);
                }
                break;
            case OPEN_IDLE:
                actuateCarriage(true);
                spinCarriage(0);
                break;
            case ZEROING:
                carriage.setSpeedToProximitySensor();
                incrementHeight(Settings.SuperStructure.ZEROING_INCREMENT_TICKS);
                if (lift.zeroLowerIfNecessary()) {
                    setHeight(0);
                    setState(State.StateEnum.CLOSED, timestamp);
                }
                break;
        }
    }

    private void setState(State.StateEnum state) {
        setState(state, Timer.getFPGATimestamp());
    }

    private void setState(State.StateEnum state, double time) {
        this.state = new State(state, time);
    }

    @Override
    public void setWantIntake() {
        setState(State.StateEnum.INTAKING);
    }

    @Override
    public void setWantClosed() {
        setState(State.StateEnum.CLOSED);
    }

    @Override
    public void setWantOpenIdle() {
        setState(State.StateEnum.OPEN_IDLE);
    }

    @Override
    public void setWantClosedOuttaking() {
        setState(State.StateEnum.OUTTAKING);
    }

    @Override
    public void setWantZero() {
        setState(State.StateEnum.ZEROING);
    }

    @Override
    public void setWantScaleHeight() {
        this.goalHeight = Settings.SuperStructure.SCALE_HEIGHT_TICKS;
    }

    @Override
    public void setWantLowHeight() {
        this.goalHeight = Settings.SuperStructure.LOW_HEIGHT_TICKS;
    }

    @Override
    public void setWantSwitchHeight() {
        this.goalHeight = Settings.SuperStructure.SWITCH_HEIGHT_TICKS;
    }

    @Override
    public void incrementHeight(int increment) {
        if (increment > 0 && lift.upperLimitSwitch()) {
            return;
        } else if (increment < 0 && lift.lowerLimitSwitch()) {
            return;
        }

        setHeight(this.goalHeight + increment);
    }

    @Override
    public int getHeight() {
        return lift.getHeight();
    }

    @Override
    public void setHeight(int setPoint) {
        this.goalHeight = setPoint;
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
    public void actuateIntakeLift(Carriage.ElevationStage stage) {
        carriage.actuateLift(stage);
    }
}
