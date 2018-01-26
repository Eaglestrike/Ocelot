package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.lib.geometry.Point;
import org.team114.lib.util.EdgeDetector;

public class ControllerInterface {

    private final EdgeDetector startLift = new EdgeDetector(this::startLift);
    private final EdgeDetector endLift = new EdgeDetector(this::endLift);
    private final EdgeDetector intake = new EdgeDetector(this::intake);
    private final EdgeDetector startClimb = new EdgeDetector(this::startClimb);
    private final EdgeDetector endClimb = new EdgeDetector(this::endClimb);
    private final EdgeDetector sensitivty = new EdgeDetector(this::sensitivity);

    private double velocityConstant = 1;
    private Joystick left;
    private Joystick right;

    public ControllerInterface(Joystick left, Joystick right) {
        this.left = left;
        this.right = right;
    }

    public void setSpeed(double a) {
        velocityConstant = a;
    }

    public double throttle() {
        return left.getY();
    }

    public double wheel() {
        return right.getX();
    }

    public double angleLeft() {
        return left.getDirectionDegrees();
    }

    public double angleRight() {
        return right.getDirectionDegrees();
    }

    /*
    This might be changed later depending on what we want speed to be
    */

    public double speed() {
        return left.getY() * velocityConstant;
    }

    /*
    This might be changed later depending on what we want the angle of the turn to be
    */

    public double getAngleOfTurn() {
        return angleRight();
    }

    public boolean startLift() {
        return left.getRawButton(1);
    }

    public boolean endLift() {
        return left.getRawButton(2);
    }

    public boolean intake() {
        return left.getRawButton(3);
    }

    public boolean quickTurn() {
        return right.getRawButton(1);
    }

    public boolean startClimb() {
        return right.getRawButton(2);
    }

    public boolean endClimb() {
        return right.getRawButton(3);
    }

    public boolean sensitivity() {
        return right.getRawButton(4);
    }
}

