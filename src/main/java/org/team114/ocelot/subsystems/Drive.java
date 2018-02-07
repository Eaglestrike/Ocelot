package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.Registry;
import org.team114.ocelot.Robot;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.modules.DriveSide;
import org.team114.ocelot.modules.Gyro;
import org.team114.lib.util.DashboardHandle;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Pose;
import org.team114.ocelot.util.Side;
import org.team114.ocelot.util.motion.PurePursuitController;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class Drive implements AbstractDrive {

    private final Map<Side, Supplier<DriveSide>> sides = new EnumMap<>(Side.class);
    private final Registry registry;

    private final double halfOfWheelbase;
    private double lastLeftAccumulated;
    private double lastRightAccumulated;

    public Drive(Registry registry) {
        this.registry = registry;
        this.sides.put(Side.LEFT, () -> registry.get(Robot.DRIVE_SIDE_LEFT));
        this.sides.put(Side.RIGHT, () -> registry.get(Robot.DRIVE_SIDE_RIGHT));
        this.halfOfWheelbase = registry.getConfiguration().getDouble("wheelbase_width_ft") / 2.0;
    }

    private Pose addPoseObservation() {
        Pose latestState = getRobotState().getLatestPose();

        double newHeading = getGyro().getYaw();
        double angle = (newHeading + latestState.getHeading()) / 2;

        double leftDistance = getDriveSide(Side.LEFT).getPosition();
        double rightDistance = getDriveSide(Side.RIGHT).getPosition();
        double leftVelocity = getDriveSide(Side.LEFT).getVelocity();
        double rightVelocity = getDriveSide(Side.RIGHT).getVelocity();

        double velocity = (leftVelocity + rightVelocity) / 2;
        double distance = (leftDistance + rightDistance - lastLeftAccumulated - lastRightAccumulated)/2;
        lastLeftAccumulated = leftDistance;
        lastRightAccumulated = rightDistance;

        getRobotState().addObservation(new Pose(
            latestState.getX() + (distance * Math.cos(angle)),
            latestState.getY() + (distance * Math.sin(angle)),
            newHeading,
            velocity
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
            (getDriveSide(Side.LEFT).getVelocity() + getDriveSide(Side.RIGHT).getVelocity())/2
        ));
    }

    @Override
    public synchronized void onStop(double timestamp) {
    }

    @Override
    public synchronized void onStep(double timestamp) {
        Pose latestPose = addPoseObservation();

        getVelocityDB().put(latestPose.getVelocity());
        getXPositionDB().put(latestPose.getX());
        getYPositionDB().put(latestPose.getY());
        getHeadingDB().put(latestPose.getHeading());
    }

    @Override
    public synchronized void setSideSpeed(Side sides, double speed) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            DriveSide driveSide = getDriveSide(side);
            driveSide.setSpeed(speed);
        }
    }

    @Override
    public synchronized void setControlMode(Side sides, ControlMode controlMode) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            DriveSide driveSide = getDriveSide(side);
            driveSide.setControlMode(controlMode);
        }
    }

    @Override
    public synchronized void setNeutralMode(Side sides, NeutralMode neutralMode) {
        // loop through sides in case it is Side.BOTH
        for (Side side : sides) {
            DriveSide driveSide = getDriveSide(side);
            driveSide.setNeutralMode(neutralMode);
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
        for (Supplier<DriveSide> driveSideSupplier : sides.values()) {
            DriveSide driveSide = driveSideSupplier.get();
            // TODO: Throw an exception instead
            for (TalonSRX talon : driveSide.getTalons()) {
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
        return registry.get(Gyro.class);
    }

    private DashboardHandle getXPositionDB() {
        return registry.get(Robot.xPositionDB);
    }

    private DashboardHandle getYPositionDB() {
        return registry.get(Robot.yPositionDB);
    }

    private DashboardHandle getHeadingDB() {
        return registry.get(Robot.headingDB);
    }

    private DashboardHandle getVelocityDB() {
        return registry.get(Robot.velocityDB);
    }

    private DriveSide getDriveSide(Side side) {
        return sides.get(side).get();
    }

    private RobotState getRobotState() {
        return registry.get(RobotState.class);
    }
}
