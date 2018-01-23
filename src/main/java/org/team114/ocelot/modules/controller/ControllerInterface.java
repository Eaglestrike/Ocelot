package org.team114.ocelot.modules.controller;

import edu.wpi.first.wpilibj.Joystick;

public class ControllerInterface {


    private Joystick left;
    private Joystick right;
    private double k;

    public ControllerInterface(int port1, int port2) {
        left = new Joystick(port1);
        right = new Joystick(port2);
        k = 1;
    }


    public double leftX() {
        return left.getX();
    }

    public double leftY() {
        return left.getY();
    }

    public double rightX() {
        return right.getX();
    }

    public double speed(){
        return leftY() * k;
    }

    public double getAngleOfTurn(){
        return angleRight();
    }

    public double rightY() {
        return right.getY();
    }

    public void setSpeed(int a) {
        k = a;
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






