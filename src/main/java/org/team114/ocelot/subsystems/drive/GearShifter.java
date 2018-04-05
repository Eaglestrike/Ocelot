package org.team114.ocelot.subsystems.drive;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team114.ocelot.subsystems.Drive;

import javax.inject.Singleton;

@Singleton
class GearShifter {

    private final DoubleSolenoid gearSolenoid;

    GearShifter(DoubleSolenoid gearSolenoid) {
        this.gearSolenoid = gearSolenoid;
    }

    public void set(Drive.State state) {
        switch (state) {
            case HIGH:
                gearSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case LOW:
                gearSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }

    public Drive.State get() {
        switch (gearSolenoid.get()) {
            case kForward:
                return Drive.State.LOW;
            case kReverse:
                return Drive.State.HIGH;
            default: // maybe throw exception?
                return Drive.State.HIGH;
        }
    }
}
