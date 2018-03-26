package org.team114.ocelot;

import org.team114.ocelot.controllers.Controller;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.util.CheesyDriveHelper;

public class TeleopInputDelegator {
    Drive drive;
    Superstructure superstructure;
    Controller controller;
    CheesyDriveHelper cheesyDrive;

    public TeleopInputDelegator(Drive drive, Superstructure superstructure, Controller controller, CheesyDriveHelper cheesyDrive) {
        this.drive = drive;
        this.superstructure = superstructure;
        this.controller = controller;
        this.cheesyDrive = cheesyDrive;
    }

    public TeleopInputDelegator(Drive drive, Superstructure superstructure, Controller controller) {
        this.drive = drive;
        this.superstructure = superstructure;
        this.controller = controller;
    }
    
    public void defaultStep() {
        //TODO break into sub methods
        // ==== DRIVER ====
        drive.setDriveSignal(cheesyDrive.cheesyDrive(controller.throttle(), controller.wheel(), controller.wantQuickTurn()));
        drive.setGear(controller.wantLowGear() ? Drive.State.LOW : Drive.State.HIGH);

        // ==== OPERATOR ====
        // carriage
        if (controller.cairrageUp()) {
            superstructure.actuateIntakeLift(Carriage.ElevationStage.RAISED);
        } else if (controller.cairrageMiddle()) {
            superstructure.actuateIntakeLift(Carriage.ElevationStage.MIDDLE);
        } else if (controller.cairrageDown()) {
            superstructure.actuateIntakeLift(Carriage.ElevationStage.LOWERED);
        }

        if (controller.carriageClose()) {
            superstructure.setWantClosed();
        } else if (controller.carriageOuttake()) {
            superstructure.setWantClosedOuttaking();
        } else if (controller.carriageIntake()) {
            superstructure.setWantIntake();
        } else if (controller.carriageOpen()) {
            superstructure.setWantOpenIdle();
        } else if (controller.liftZeroCalibration()) {
            superstructure.setWantZero();
        }

        // update speeds
        double liftSpeed;
        //TODO refactor into constants
        switch ((controller.speedFaster() ? 1 : 0) +
                (controller.speedSlower() ? -1 : 0)) {
            case 1:
                superstructure.setOuttakeSpeed(Settings.Carriage.OUTTAKE_SPEED_FAST);
                liftSpeed = 0.75;
                break;
            case -1:
                superstructure.setOuttakeSpeed(Settings.Carriage.OUTTAKE_SPEED_SLOW);
                liftSpeed = 0.25;
                break;
            default:
                liftSpeed = 0.5;
                superstructure.setOuttakeSpeed(Settings.Carriage.OUTTAKE_SPEED_NORMAL);
                break;
        }

        if (controller.wantManualLiftHeight()) {
            superstructure.setManualControl(liftSpeed *
                    (controller.manualLiftUp() ? 1 : 0) +
                    (controller.manualLiftDown() ? -1 : 0));
        } else { // not manual control
            superstructure.setHeightFraction(controller.liftHeightSetPoint());
        }
    }
}
