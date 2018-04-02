package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Superstructure;

public class TriggerIntakeOneShotAction extends OneShotAction {

    private final Superstructure.State.StateEnum status;
    private final Superstructure superstructure;
    private final double outtakeCommand;

    public TriggerIntakeOneShotAction(Superstructure sstruct, Superstructure.State.StateEnum state, double outtakeCommand) {
        this.status = state;
        superstructure = sstruct;
        this.outtakeCommand = outtakeCommand;
    }

    @Override
    public void start() {
        switch (status) {
            case CLOSED:
                superstructure.setWantClosed();
                break;
            case OPEN_IDLE:
                superstructure.setWantOpenIdle();
                break;
            case OUTTAKING:
                superstructure.setOuttakeSpeed(outtakeCommand);
                superstructure.setWantClosedOuttaking();
                break;
            case INTAKING:
                superstructure.setWantIntake();
                break;
            case ZEROING:
                superstructure.setWantZero();
                break;
            case OPEN_LOW_SPEED_DROP:
                superstructure.setWantOpenLowSpeed();
                break;
        }
    }
}
