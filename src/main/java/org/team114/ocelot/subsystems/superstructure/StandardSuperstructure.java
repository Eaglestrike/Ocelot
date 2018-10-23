package org.team114.ocelot.subsystems.superstructure;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.lib.util.DashboardHandle;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Superstructure;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StandardSuperstructure implements Superstructure {

    private State state;

    private final DashboardHandle currentHeightDB = new DashboardHandle("Current Height");
    private final DashboardHandle goalHeightDB = new DashboardHandle("Goal Height");
    private final DashboardHandle distanceSensorDB = new DashboardHandle("Distance To 'Box'");


    private final Carriage carriage;
    private final Lift lift;

    private int goalHeight;
    private double outtakeSpeed;


    @Inject
    StandardSuperstructure(Carriage carriage, Lift lift) {
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
        SmartDashboard.putString("sstruct mode", state.state.name());
        distanceSensorDB.put(carriage.getDistance());

        // handle intake transitions
        switch (state.state) {
            case CLOSED:
                actuateCarriage(false);
                carriage.setSpeedToProximitySensor();
                break;
            case INTAKING:
                actuateIntakeLift(CarriageElevationStage.LOWERED);
                actuateCarriage(true);
                spinCarriage(Settings.Carriage.INTAKE_IN_COMMAND);
                break;
            case OUTTAKING:
                spinCarriage(outtakeSpeed);
//                System.out.println(timestamp + " : " + state.timestamp);
                if (timestamp - state.timestamp > Settings.Carriage.OUTTAKE_TIME_SECONDS) {
                    setState(State.StateEnum.CLOSED, timestamp);
                }
                break;
            case OPEN_LOW_SPEED_DROP:
                actuateCarriage(true);
                if (Timer.getFPGATimestamp() - state.timestamp < 0.5) {
                    spinCarriage(Settings.Carriage.OUTTAKE_COMMAND_RISING_OPEN_IDLE);
                } else {
                    spinCarriage(0);
                }
                break;
            case OPEN_IDLE:
                actuateCarriage(true);
                spinCarriage(0);
                break;
            case ZEROING:
                carriage.setSpeedToProximitySensor();
                setHeight(getHeight() + Settings.SuperStructure.ZEROING_INCREMENT_TICKS);
                if (lift.zeroLowerIfNecessary()) {
                    setHeight(0);
                    setState(State.StateEnum.CLOSED, timestamp);
                }
                break;
        }
    }

    @Override
    public void setState(State.StateEnum state) {
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
    public void setWantOpenLowSpeed() {
        setState(State.StateEnum.OPEN_LOW_SPEED_DROP);
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
    public boolean isZeroing() {
        return this.state.state == State.StateEnum.ZEROING;
    }

    @Override
    public void actuateIntakeLift(CarriageElevationStage stage) {
        carriage.actuateLift(stage);
    }

    @Override
    public void setOuttakeSpeed(double command) {
        this.outtakeSpeed = command;
    }

    @Override
    public void setHeightFraction(double fraction) {
        // TODO dyanmically updating top
        setHeight((int)(Settings.Lift.MAX_HEIGHT_TICKS * fraction));
    }

    @Override
    public void setManualControl(double speed) {
        if (Epsilon.epsilonEquals(speed, 0)) {
            lift.goToHeight(lift.getHeight());
            return;
        }
        lift.manualControl(speed);
    }
}
