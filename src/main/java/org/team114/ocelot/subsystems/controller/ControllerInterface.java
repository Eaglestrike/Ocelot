package org.team114.ocelot.subsystems.controller;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.lib.subsystem.Subsystem;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.event.EventQueue;

public final class ControllerInterface implements Subsystem {


    private final Joystick left;
    private final Joystick right;
    private final EventQueue eventQueue;
    private boolean changeInRightX, changeInRightY, changeInLeftX, changeInLeftY;
    private double currentRightX, currentLeftX, currentRightY, currentLeftY;
    private double epsilonError = 0.0001;

    public ControllerInterface(int port1, int port2, EventQueue eventQueue) {
        left = new Joystick(port1);
        right = new Joystick(port2);
        this.eventQueue = eventQueue;
    }


    public double leftX() {
        currentLeftX = left.getX();
        return currentLeftX;
    }

    public double leftY() {
        currentLeftY = left.getY();
        return currentLeftY;
    }

    public double rightX() {
        currentRightX = right.getX();
        return currentRightX;
    }

    public double rightY() {
        currentRightY = right.getY();
        return currentRightY;
    }

    public double getAngleOfTurn() {
        return angleRight();
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


    private void checkRightX() {
        changeInRightX = !Epsilon.epsilonEquals(currentRightX, right.getX(), epsilonError);

    }

    private void checkRightY() {
        changeInRightX = !Epsilon.epsilonEquals(currentRightY, right.getY(), epsilonError);
    }

    private void checkLeftX() {
        changeInLeftX = !Epsilon.epsilonEquals(currentLeftX, left.getX(), epsilonError);
    }

    private void checkLeftY() {
        changeInLeftY = !Epsilon.epsilonEquals(currentLeftY, left.getY(), epsilonError);
    }


    private void reset() {
    }

    @Override
    public void onStart(double timestamp) {
        reset();
    }

    @Override
    public void onStop(double timestamp) {
        reset();
    }

    @Override
    public void onStep(double timestamp) {

        checkLeftX();
        checkLeftY();
        checkRightX();
        checkRightY();

        if (changeInRightX || changeInRightY) {
            ControllerEvent controllerEvent = new ControllerEvent(ControllerType.RIGHT_JOYSTICK, currentRightX, currentRightY, 0.0);
            eventQueue.push(controllerEvent);
        }

        if (changeInLeftX || changeInLeftY) {
            ControllerEvent controllerEvent = new ControllerEvent(ControllerType.LEFT_JOYSTICK, currentLeftX, currentLeftY, 0.0);
            eventQueue.push(controllerEvent);
        }

        leftX();
        leftY();
        rightX();
        rightY();
    }
}






