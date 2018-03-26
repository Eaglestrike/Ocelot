package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Superstructure;

public class TriggerIntakeOneShotAction extends OneShotAction {

    public enum Status {
        CLOSED, OPEN_IDLE, OUTTAKING, INTAKING
    }

    public final Status status;
    private final Superstructure superstructure;

    public TriggerIntakeOneShotAction(Superstructure sstruct, Status status) {
        this.status = status;
        superstructure = sstruct;
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
                superstructure.setWantClosedOuttaking();
                break;
            case INTAKING:
                superstructure.setWantIntake();
                break;
        }
    }

}
