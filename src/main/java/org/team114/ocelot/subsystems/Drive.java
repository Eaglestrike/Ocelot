package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.Robot;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.modules.GearShifter;
import org.team114.ocelot.modules.Gyro;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.util.DashboardHandle;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Pose;
import org.team114.ocelot.util.Side;
import org.team114.ocelot.util.motion.PurePursuitController;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class Drive implements AbstractDrive {

    private final Map<Side, Supplier<RobotSide>> robotSideMap = new EnumMap<>(Side.class);
    private final RobotRegistry robotRegistry;

    // TODO change from practice base
    private final Encoder leftEncoder;
    private final Encoder rightEncoder;
    private final double halfOfWheelbase;

    public Drive(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
        this.robotSideMap.put(Side.LEFT, () -> robotRegistry.get(Robot.ROBOT_SIDE_LEFT));
        this.robotSideMap.put(Side.RIGHT, () -> robotRegistry.get(Robot.ROBOT_SIDE_RIGHT));
        this.halfOfWheelbase = robotRegistry.getConfiguration().getDouble("wheelbase_width_ft") / 2.0;

        // TODO practice base
        RobotSettings.Configuration configuration = this.robotRegistry.getConfiguration();
        leftEncoder = new Encoder(
                configuration.getChannelAndRegister("left.channelA"),
                configuration.getChannelAndRegister("left.channelB"),
                configuration.getBoolean("left.reversedDirection"));
        leftEncoder.setDistancePerPulse(configuration.getDouble("left.distancePerPulseInFeet")); //ft
        rightEncoder = new Encoder(
                configuration.getChannelAndRegister("right.channelA"),
                configuration.getChannelAndRegister("right.channelB"),
                configuration.getBoolean("right.reversedDirection"));
        rightEncoder.setDistancePerPulse(configuration.getDouble("right.distancePerPulseInFeet")); //ft
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
        Pose latestState = getRobotState().getLatestPose();

        double newHeading = getGyro().getYaw();
        double angle = (newHeading + latestState.getHeading()) / 2;

        double L = getLeftEncoder();
        double R = getRightEncoder();

        double distance = (L + R - leftAccum - rightAccum)/2;
        leftAccum = L;
        rightAccum = R;

        getRobotState().addObservation(new Pose(
            latestState.getX() + (distance * Math.cos(angle)),
            latestState.getY() + (distance * Math.sin(angle)),
            newHeading,
            (leftEncoder.getRate() + rightEncoder.getRate())/2
        ));

        return getRobotState().getLatestPose();
    }

    @Override
    public synchronized void onStart(double timestamp) {
        getGyro().init();
        setControlMode(Side.BOTH, ControlMode.PercentOutput);
        setSideSpeed(Side.BOTH, 0);
        getRobotState().addObservation(new Pose(0, 0,
                getGyro().getYaw(),
                (leftEncoder.getRate() + rightEncoder.getRate())/2)
        );
    }

    @Override
    public synchronized void onStop(double timestamp) {
    }

    @Override
    public synchronized void onStep(double timestamp) {
        Pose latestPose = this.addPoseObservation();

        getVelocityDB().put(latestPose.getVel());
        getxPositionDB().put(latestPose.getX());
        getyPositionDB().put(latestPose.getY());
        getHeadingDB().put(latestPose.getHeading());
    }

    @Override
    public synchronized void setSideSpeed(Side sides, double speed) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            RobotSide robotSide = getRobotSide(side);
            robotSide.setSpeed(speed);
        }
    }

    @Override
    public synchronized void setControlMode(Side sides, ControlMode controlMode) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            RobotSide robotSide = getRobotSide(side);
            robotSide.setControlMode(controlMode);
        }
    }

    @Override
    public synchronized void setNeutralMode(Side sides, NeutralMode neutralMode) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            RobotSide robotSide = getRobotSide(side);
            robotSide.setNeutralMode(neutralMode);
        }
    }

    @Override
    public void setDriveSignal(DriveSignal signal) {
        setControlMode(Side.BOTH, ControlMode.PercentOutput);
        setSideSpeed(Side.LEFT, signal.getLeft());
        setSideSpeed(Side.RIGHT, -signal.getRight());
    }

    @Override
    public void setDriveArcCommand(PurePursuitController.DriveArcCommand a) {
        double Kv = 1/9.5; //TODO replace primitive speed controller with talon velocity control on main robot
        double L, R;
        if (Epsilon.epsilonEquals(a.curvature, 0)) {
            L = Kv * a.vel;
            R = L;
        } else {
            L = Kv * a.vel * a.curvature * (1/a.curvature + halfOfWheelbase);
            R = Kv * a.vel * a.curvature * (1/a.curvature - halfOfWheelbase);
        }
        setSideSpeed(Side.LEFT, L);
        setSideSpeed(Side.RIGHT, -R);
    }

    @Override
    public synchronized void selfTest() {
        for (Supplier<RobotSide> robotSideSupplier : robotSideMap.values()) {
            RobotSide robotSide = robotSideSupplier.get();
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

    private Gyro getGyro() {
        return this.robotRegistry.get(Gyro.class);
    }

    private DashboardHandle getxPositionDB() {
        return this.robotRegistry.get(Robot.DB_xPositionDB);
    }

    private DashboardHandle getyPositionDB() {
        return this.robotRegistry.get(Robot.DB_yPositionDB);
    }

    private DashboardHandle getHeadingDB() {
        return this.robotRegistry.get(Robot.DB_headingDB);
    }

    private DashboardHandle getVelocityDB() {
        return this.robotRegistry.get(Robot.DB_velocityDB);
    }

    private RobotSide getRobotSide(Side side) {
        return this.robotSideMap.get(side).get();
    }

    private RobotState getRobotState() {
        return this.robotRegistry.get(RobotState.class);
    }
}
