package org.team114.ocelot;

import org.team114.ocelot.subsystems.drive.AbstractDrive;

//Pat has plans to make this better, I just need it to test other functionality
public class Subsystems {
    private AbstractDrive drive;
    private RobotState state;

    public Subsystems() {}

    public AbstractDrive getDrive() {
        return drive;
    }

    public RobotState getState() {
        return state;
    }

    // package-private
    void setDrive(AbstractDrive drive) {
        this.drive = drive;
    }

    void setState(RobotState state) {
        this.state = state;
    }
}
