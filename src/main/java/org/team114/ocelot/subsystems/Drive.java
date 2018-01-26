package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.modules.Gyro;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.util.Pose;
import org.team114.ocelot.util.Side;

import java.util.EnumMap;
import java.util.Map;

public class Drive implements AbstractDrive {

    private final Map<Side, RobotSide> robotSideMap = new EnumMap<>(Side.class);
    private Gyro gyro;
    private Pose latestState = new Pose(0, 0, 0);
    private double lastTimeStamp;

    public Drive(RobotSide leftSide, RobotSide rightSide, Gyro gyro) {
        this.robotSideMap.put(Side.LEFT, leftSide);
        this.robotSideMap.put(Side.RIGHT, rightSide);
        this.gyro = gyro;
    }

    @Override
    public synchronized void onStart(double timestamp) {
        gyro.waitUntilCalibrated();
        gyro.zeroYaw();
        reset();
        lastTimeStamp = timestamp;
    }

    @Override
    public synchronized void onStop(double timestamp) {
        reset();
    }

    @Override
    public synchronized void onStep(double timestamp) {
        double delta = timestamp - lastTimeStamp;
        lastTimeStamp = timestamp;

        double newHeading = gyro.getYaw();

        double leftDistance = robotSideMap.get(Side.LEFT).getEncoderDistance();
        double rightDistance = robotSideMap.get(Side.RIGHT).getEncoderDistance();

        double angle = (newHeading + latestState.getHeading()) / 2;
        double distance = (leftDistance + rightDistance) / 2;

        latestState = new Pose(
            latestState.getX() + (distance * Math.cos(angle)),
            latestState.getY() + (distance * Math.sin(angle)),
            newHeading
        );

        SmartDashboard.putNumber("x", latestState.getX());
        SmartDashboard.putNumber("y", latestState.getY());
        SmartDashboard.putNumber("heading", latestState.getHeading());
    }

    private synchronized void reset() {
        setControlMode(Side.BOTH, ControlMode.PercentOutput);
        setSideSpeed(Side.BOTH, 0);
        latestState = new Pose(0, 0, latestState.getHeading());
    }

    @Override
    public synchronized Pose getLatestState() {
        return latestState;
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
            // TODO: Throw an exception instead
            for (TalonSRX talon : robotSide.getTalons()) {
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
