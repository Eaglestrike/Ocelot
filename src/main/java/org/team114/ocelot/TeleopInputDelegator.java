package org.team114.ocelot;

import org.team114.ocelot.controllers.Controller;
import org.team114.ocelot.modules.CarriageElevationStage;
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
    
    public void defaultStep() {
        //TODO break into sub methods
        // ==== DRIVER ====
        drive.setDriveSignal(cheesyDrive.cheesyDrive(-controller.throttle(), -controller.wheel(), controller.wantQuickTurn()));

        drive.setGear(controller.wantLowGear() ? Drive.State.LOW : Drive.State.HIGH);

        // ==== OPERATOR ====
        // carriage
        if (controller.cairrageUp()) {
            superstructure.actuateIntakeLift(CarriageElevationStage.RAISED);
        } else if (controller.cairrageMiddle()) {
            superstructure.actuateIntakeLift(CarriageElevationStage.MIDDLE);
        } else if (controller.cairrageDown()) {
            superstructure.actuateIntakeLift(CarriageElevationStage.LOWERED);
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
                superstructure.setOuttakeSpeed(Settings.Carriage.OUTTAKE_COMMAND_FAST);
                liftSpeed = 1;
                break;
            case -1:
                superstructure.setOuttakeSpeed(Settings.Carriage.OUTTAKE_COMMAND_SLOW);
                liftSpeed = 0.25;
                break;
            default:
                liftSpeed = 0.5;
                superstructure.setOuttakeSpeed(Settings.Carriage.OUTTAKE_COMMAND_NORMAL);
                break;
        }

        if (controller.wantManualLiftHeight()) {
            superstructure.setManualControl(liftSpeed * (
                    (controller.manualLiftUp() ? 1 : 0) +
                    (controller.manualLiftDown() ? -1 : 0)));
        } else { // do not want manual control
            superstructure.setHeightFraction((1 + controller.liftHeightSetPoint()) / 2.0);
        }
    }
}
