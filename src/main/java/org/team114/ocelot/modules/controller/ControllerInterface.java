package org.team114.ocelot.modules.controller;

import edu.wpi.first.wpilibj.Joystick;

public class ControllerInterface {


    private Joystick left;
    private Joystick right;

    public ControllerInterface(int port1, int port2) {
        left = new Joystick(port1);
        right = new Joystick(port2);
    }


    //Is there really a need for all of these?
    public double leftX() {
        return left.getX();
    }

    public double leftY() {
        return left.getY();
    }

    public double rightX() {
        return right.getX();
    }

    public double rightY() {
        return right.getY();
    }

    public double angleRight() {
        return right.getDirectionDegrees();
    }

    public double angleLeft() {
        return left.getDirectionDegrees();
    }

    public boolean liftStatus() {
        return left.getRawButtonPressed(1);
    }

    public boolean intakeStatus() {
        return left.getRawButtonPressed(2);
    }

    public boolean climbStatus() {
        return right.getRawButtonPressed(1);
    }

    public boolean checkLiftChange() {
        return left.getRawButtonPressed(1);
    }

    public boolean checkIntakeChange() {
        return left.getRawButtonPressed(2);
    }

    public boolean checkClimbChange() {
        return right.getRawButtonPressed(3);
    }

}






