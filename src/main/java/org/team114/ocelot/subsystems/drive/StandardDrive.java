package org.team114.ocelot.subsystems.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.lib.util.DashboardHandle;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.dagger.Left;
import org.team114.ocelot.dagger.Right;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Pose;
import org.team114.ocelot.util.motion.PurePursuitController;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StandardDrive implements Drive {

    private final DashboardHandle xPositionDB = new DashboardHandle("Pose X");
    private final DashboardHandle yPositionDB = new DashboardHandle("Pose Y");
    private final DashboardHandle headingDB = new DashboardHandle("Pose hdg");
    private final DashboardHandle velocityDB = new DashboardHandle("Pose vel");

    private final RobotState robotState;

    private final DriveSide leftSide;
    private final DriveSide rightSide;

    private final Gyro gyro;
    private final GearShifter shifter;

    private final double halfOfWheelbase = Settings.Drive.WHEELBASE_WIDTH_FT / 2.0;
    private double lastLeftAccumulated;
    private double lastRightAccumulated;

    @Inject
    StandardDrive(RobotState robotState, Gyro gyro, @Left DriveSide leftSide,
                  @Right DriveSide rightSide, GearShifter shifter) {

        this.robotState = robotState;
        this.gyro = gyro;

        this.leftSide = leftSide;
        this.rightSide = rightSide;

        this.shifter = shifter;

        leftSide.setInverted(true);
        prepareForAuto();
    }

    private Pose addPoseObservation() {
        Pose latestState = robotState.getPose();

        double newHeading = gyro.getYaw();
        double angle = (newHeading + latestState.getHeading()) / 2;

        // we have to use this function to get position so that sensorPhase is taken into account
        // undocumented behavior in Phoenix
        double leftDistance = leftSide.getPosition();
        double rightDistance = rightSide.getPosition();

        double leftVelocity = leftSide.getVelocity();
        double rightVelocity = rightSide.getVelocity();

        double velocity = (leftVelocity + rightVelocity) / 2;
        double distance = (leftDistance + rightDistance - lastLeftAccumulated - lastRightAccumulated) / 2;
        lastLeftAccumulated = leftDistance;
        lastRightAccumulated = rightDistance;
        SmartDashboard.putNumber("left enc", lastLeftAccumulated);
        SmartDashboard.putNumber("right enc", lastRightAccumulated);

        robotState.addObservation(new Pose(
            latestState.getX() + (distance * Math.cos(angle)),
            latestState.getY() + (distance * Math.sin(angle)),
            newHeading,
            velocity
        ));

        return robotState.getPose();
    }

    @Override
    public synchronized void onStart(double timestamp) {
        gyro.init();
        leftSide.setPercentOutput(0);
        rightSide.setPercentOutput(0);

        robotState.addObservation(new Pose(0, 0,
            gyro.getYaw(),
            0
        ));
    }

    @Override public synchronized void onStop(double timestamp) {}

    @Override
    public synchronized void onStep(double timestamp) {
        Pose latestPose = addPoseObservation();
        velocityDB.put(latestPose.getVelocity());
        xPositionDB.put(latestPose.getX());
        yPositionDB.put(latestPose.getY());
        headingDB.put(latestPose.getHeading());
    }

    @Override
    public void zeroAllSensors() {
        // zero gyro and position tracking
        gyro.reset();
        robotState.addObservation(new Pose(0,0,gyro.getYaw(), 0));
    }

    @Override
    public synchronized void setGear(State state) {
        shifter.set(state);
    }

    @Override
    public synchronized State getGear() {
        return shifter.get();
    }

    @Override
    public void setDriveSignal(DriveSignal signal) {
        leftSide.setPercentOutput(signal.getLeft());
        rightSide.setPercentOutput(signal.getRight());
    }

    @Override
    public void setDriveArcCommand(PurePursuitController.DriveArcCommand a) {
//        double L, R;
//        // convert TICKS / 100ms to ft/s
//        double vel = a.vel * 10 * Settings.Drive.DRIVE_ENCODER_TICKS_PER_FOOT;
//        if (Epsilon.epsilonEquals(a.curvature, 0)) {
//            L = R = vel;
//        } else {
//            L = vel * a.curvature * (1/a.curvature + halfOfWheelbase);
//            R = vel * a.curvature * (1/a.curvature - halfOfWheelbase);
//        }
////        //TODO replace with vel config
//
//        leftSide.setVelocity(L);
//        rightSide.setVelocity(R); //TODO check
        double Kv = 1/9.5; //TODO replace primitive speed controller with talon velocity control on main robot
        double L, R;
        if (Epsilon.epsilonEquals(a.curvature, 0)) {
            L = Kv * a.vel;
            R = L;
        } else {
            L = Kv * a.vel * a.curvature * (1/a.curvature + halfOfWheelbase);
            R = Kv * a.vel * a.curvature * (1/a.curvature - halfOfWheelbase);
        }
        //TODO replace with vel config
        leftSide.setPercentOutput(L);
        rightSide.setPercentOutput(R);
    }

    @Override
    public synchronized void prepareForAuto() {
        leftSide.configureForAuto();
        rightSide.configureForAuto();
    }

    @Override
    public synchronized void prepareForTeleop() {
        leftSide.configureForTeleop();
        rightSide.configureForTeleop();
    }
}
