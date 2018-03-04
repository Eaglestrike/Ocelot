package org.team114.ocelot.controllers;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Represents a physical Xbox controller. This is at a different level of abstraction from
 * the other controllers, which map functionality, not buttons.
 */
public class XboxController {
    private final Joystick stick;

    public XboxController(Joystick stick) {
        this.stick = stick;
    }

    // note that buttons start at 1, while axises start at 0.
    public boolean a() {
        return stick.getRawButton(1);
    }

    public boolean b() {
        return stick.getRawButton(2);
    }

    public boolean x() {
        return stick.getRawButton(3);
    }

    public boolean y() {
        return stick.getRawButton(4);
    }

    public boolean back() {
        return stick.getRawButton(7);
    }

    public boolean start() {
        return stick.getRawButton(8);
    }

    public boolean leftBumper() {
        return stick.getRawButton(5);
    }

    public boolean rightBumper() {
        return stick.getRawButton(6);
    }

    public boolean leftStickPressed() {
        return stick.getRawButton(9);
    }

    public boolean rightStickPressed() {
        return stick.getRawButton(10);
    }

    /**
     * Gets the left trigger value.
     * @return trigger value, always positive
     */
    public double leftTrigger() {
        return stick.getRawAxis(2);
    }

    /**
     * Gets the right trigger value.
     * @return trigger value, always positive
     */
    public double rightTrigger() {
        return stick.getRawAxis(3);
    }

    public double leftXAxis() {
        return stick.getRawAxis(0);
    }

    public double leftYAxis() {
        return stick.getRawAxis(1);
    }

    public double rightXAxis() {
        return stick.getRawAxis(4);
    }

    public double rightYAxis() {
        return stick.getRawAxis(5);
    }

    public int arrowPad() {
        return stick.getPOV(0);
    }

}
