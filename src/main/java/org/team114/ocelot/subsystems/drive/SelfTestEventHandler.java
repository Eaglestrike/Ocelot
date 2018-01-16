package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class SelfTestEventHandler implements EventHandler {

    Drive drive;

    public SelfTestEventHandler(Drive drive) {
        this.drive = drive;
    }

    public void handle(SelfTestEvent event) {
        TalonSRX[] talons = drive.getTalons();
        for (TalonSRX talon: talons) {
            int id = talon.getDeviceID();
            if (id == 0) {
                System.out.println("Talon " + id + " has not been configured.");
            }
            else if (id > 62 || id < 1) {
                System.out.println("Talon " + id + " has an ID that is outside of ID bounds.");
            }
        }
    }
}
