package org.team114.ocelot.subsystems.controller;

import org.team114.ocelot.event.AbstractEvent;

public class ControllerEvent extends AbstractEvent{

    private final double angle;
    private final ControllerType controllerType;
    private final double x;
    private final double y;

    ControllerEvent(ControllerType controllerType, double x, double y, double angle){
        this.controllerType = controllerType;
        this.x=x;
        this.y=y;
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public ControllerType getControllerType() {
        return controllerType;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
