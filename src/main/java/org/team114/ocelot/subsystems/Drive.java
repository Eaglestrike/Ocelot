package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.modules.Gyro;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.util.DashboardHandle;
import org.team114.ocelot.util.Pose;
import org.team114.ocelot.util.Side;

import java.util.EnumMap;
import java.util.Map;

public class Drive implements AbstractDrive {

    private static DashboardHandle xPositionDB = new DashboardHandle("Pose X");
    private static DashboardHandle yPositionDB = new DashboardHandle("Pose Y");
    private static DashboardHandle headingDB = new DashboardHandle("Pose hdg");

    private final Map<Side, RobotSide> robotSideMap = new EnumMap<>(Side.class);
    private Gyro gyro;
    private RobotState robotState;

    // TODO change from practice base
    Encoder leftEncoder;
    Encoder rightEncoder;

    public Drive(RobotSide leftSide, RobotSide rightSide, Gyro gyro, RobotState robotState) {
        this.robotSideMap.put(Side.LEFT, leftSide);
        this.robotSideMap.put(Side.RIGHT, rightSide);
        this.gyro = gyro;
        this.robotState = robotState;

        // TODO practice base
        leftEncoder = new Encoder(5,6, true);
        rightEncoder = new Encoder(7,8, false);
        leftEncoder.setDistancePerPulse(0.043946541); //ft
        rightEncoder.setDistancePerPulse(0.043946541);
    }


    // TODO for the practice base, these must be changed later
    private double getLeftEncoder() {
        return leftEncoder.getDistance();
    }

    private double getRightEncoder() {
        return rightEncoder.getDistance();
    }

    private double leftAccum = 0;
    private double rightAccum = 0;

    private Pose addPoseObservation() {
        Pose latestState = robotState.getLatestPose();

        double newHeading = gyro.getYaw();
        double angle = (newHeading + latestState.getHeading()) / 2;

        double L = getLeftEncoder();
        double R = getRightEncoder();

        double distance = (L + R - leftAccum - rightAccum)/2;
        leftAccum = L;
        rightAccum = R;

        robotState.addObservation(new Pose(
            latestState.getX() + (distance * Math.cos(angle)),
            latestState.getY() + (distance * Math.sin(angle)),
            newHeading,
            (leftEncoder.getRate() + rightEncoder.getRate())/2
        ));

        return robotState.getLatestPose();
    }

    @Override
    public synchronized void onStart(double timestamp) {
        gyro.waitUntilCalibrated();
        gyro.zeroYaw();
        setControlMode(Side.BOTH, ControlMode.PercentOutput);
        setSideSpeed(Side.BOTH, 0);
        robotState.addObservation(new Pose(0, 0,
                gyro.getYaw(),
                (leftEncoder.getRate() + rightEncoder.getRate())/2)
        );
    }

    @Override
    public synchronized void onStop(double timestamp) {
    }

    @Override
    public synchronized void onStep(double timestamp) {
        Pose latestPose = this.addPoseObservation();

        xPositionDB.put(latestPose.getX());
        yPositionDB.put(latestPose.getY());
        headingDB.put(latestPose.getHeading());
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
