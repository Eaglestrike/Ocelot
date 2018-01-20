package org.team114.ocelot.modules.controller;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.event.Event;

public class ControllerInterface {

    private double lastX_LEFT, lastY_LEFT, lastX_RIGHT, lastY_RIGHT, lastZ;

    private Joystick left;
    private Joystick right;

    private static final int LEFT = 0, RIGHT = 1;

    public ControllerInterface(int port1, int port2) {
        left = new Joystick(port1);
        right = new Joystick(port2);


        lastX_LEFT = this.getX(LEFT);
        lastY_LEFT = this.getY(LEFT);
        lastX_RIGHT = this.getX(RIGHT);
        lastY_RIGHT = this.getY(RIGHT);
        //lastZ = this.getZ();
    }


    //Is there really a need for all of these?
    public double getX(int a) {
        // true is right, false is left
        if(a == RIGHT){
            return right.getX();
        } else if (a == LEFT){
            return left.getX();
        } else {
            throw new IllegalArgumentException("Not a valid string input");
        }
    }

    public double getY(int a) {
        // true is right, false is left
        if(a == RIGHT){
            return right.getY();
        } else if (a == LEFT){
            return left.getY();
        } else {
            throw new IllegalArgumentException("Not a valid string input");
        }
    }

//    public double getZ(){
//        return r
//    }

    public double getAngleRight() {
        return right.getDirectionDegrees();
    }


    public double getMagnitudeLeft() {
        return getY(LEFT);
    }

    public boolean getLiftStatus() {
        return left.getRawButtonPressed(1);
    }

    public boolean getIntakeStatus() {
        return left.getRawButtonPressed(2);
    }

    public boolean getClimbStatus() {
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






