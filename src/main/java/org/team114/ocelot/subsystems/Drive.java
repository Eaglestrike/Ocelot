package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.util.Side;

import java.util.EnumMap;
import java.util.Map;

public class Drive implements AbstractDrive, Subsystem {

    private final Map<Side, RobotSide> robotSideMap = new EnumMap<>(Side.class);

    public Drive(RobotSide leftSide, RobotSide rightSide) {
        robotSideMap.put(Side.LEFT, leftSide);
        robotSideMap.put(Side.RIGHT, rightSide);
    }

    @Override
    public synchronized void onStart(double timestamp) {
        reset();
    }

    @Override
    public synchronized void onStop(double timestamp) {
        reset();
    }

    @Override
    public synchronized void onStep(double timestamp) {
    }

    private synchronized void reset() {
        setControlMode(Side.BOTH, ControlMode.PercentOutput);
        setSideSpeed(Side.BOTH, 0);
    }

    @Override
    public synchronized void setSideSpeed(Side sides, double speed) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            RobotSide robotSide = robotSideMap.get(side);
            robotSide.setSpeed(speed);
        }
    }

    @Override
    public synchronized void setControlMode(Side sides, ControlMode controlMode) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            RobotSide robotSide = robotSideMap.get(side);
            robotSide.setControlMode(controlMode);
        }
    }

    @Override
    public synchronized void setNeutralMode(Side sides, NeutralMode neutralMode) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            RobotSide robotSide = robotSideMap.get(side);
            robotSide.setNeutralMode(neutralMode);
        }
    }

    @Override
    public synchronized void selfTest() {
        for (RobotSide robotSide : robotSideMap.values()) {
            // TODO: Publish an error event instead
            for (TalonSRX talon : robotSide.getTalonSRXs()) {
                int id = talon.getDeviceID();
                if (id == 0) {
                    System.out.println("Talon " + id + " has not been configured.");
                } else if (id > 62 || id < 1) {
                    System.out.println("Talon " + id + " has an ID that is outside of ID bounds.");
                }
            }
        }
    }
}
