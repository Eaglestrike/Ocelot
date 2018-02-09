package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.Robot;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.modules.GearShifter;
import org.team114.ocelot.modules.Gyro;
import org.team114.lib.util.DashboardHandle;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Pose;
import org.team114.ocelot.util.motion.PurePursuitController;

public class Drive implements AbstractDrive {

    private final DashboardHandle xPositionDB = new DashboardHandle("Pose X");
    private final DashboardHandle yPositionDB = new DashboardHandle("Pose Y");
    private final DashboardHandle headingDB = new DashboardHandle("Pose hdg");
    private final DashboardHandle velocityDB = new DashboardHandle("Pose vel");
    // drive train talons
    private TalonSRX lMaster;
    private TalonSRX rMaster;
    private TalonSRX lSlave;
    private TalonSRX rSlave;

    private final double ENCODER_FEET_PER_TICK = RobotSettings.DRIVE_ENCODER_FEET_PER_TICK;
    private final double halfOfWheelbase;
    private double lastLeftAccumulated;
    private double lastRightAccumulated;

    private final RobotRegistry robotRegistry;

    public Drive(RobotRegistry robotRegistry, TalonSRX lMaster, TalonSRX lSlave, TalonSRX rMaster, TalonSRX rSlave) {
        // configure talons
        this.lMaster = lMaster;
        this.rMaster = rMaster;
        this.lSlave = lSlave;
        this.rSlave = rSlave;
        initTalons();
        configureTalonsForAuto();

        this.robotRegistry = robotRegistry;
        this.halfOfWheelbase = robotRegistry.getConfiguration().getDouble("wheelbase_width_ft") / 2.0;
    }

    private Pose addPoseObservation() {
        Pose latestState = getRobotState().getLatestPose();

        double newHeading = getGyro().getYaw();
        double angle = (newHeading + latestState.getHeading()) / 2;

        // we have to use this function to get position so that sensorPhase is taken into account
        // undocumented behavior in Phoenix
        double leftDistance = lMaster.getSelectedSensorPosition(0);
        double rightDistance = rMaster.getSelectedSensorPosition(0);

        SmartDashboard.putNumber("L Enc", leftDistance);
        SmartDashboard.putNumber("R Enc", rightDistance);
        double leftVelocity = lMaster.getSelectedSensorVelocity(0);;
        double rightVelocity = rMaster.getSelectedSensorVelocity(0);;

        double velocity = (leftVelocity + rightVelocity) / 2 * ENCODER_FEET_PER_TICK;
        double distance = (leftDistance + rightDistance - lastLeftAccumulated - lastRightAccumulated) / 2 * ENCODER_FEET_PER_TICK;
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
        lMaster.set(ControlMode.PercentOutput, 0);
        rMaster.set(ControlMode.PercentOutput, 0);

        getRobotState().addObservation(new Pose(0, 0,
            getGyro().getYaw(),
            0
        ));
    }

    @Override
    public synchronized void onStop(double timestamp) {
    }

    @Override
    public synchronized void onStep(double timestamp) {
        Pose latestPose = addPoseObservation();
        velocityDB.put(latestPose.getVelocity());
        xPositionDB.put(latestPose.getX());
        yPositionDB.put(latestPose.getY());
        headingDB.put(latestPose.getHeading());
    }

    @Override
    public synchronized void setGear(GearShifter.State state) {
        robotRegistry.get(GearShifter.class).set(state);
    }

    @Override
    public void setDriveSignal(DriveSignal signal) {
        lMaster.set(ControlMode.PercentOutput, signal.getLeft());
        rMaster.set(ControlMode.PercentOutput, signal.getRight());
        SmartDashboard.putNumber("left cmd", signal.getLeft());
        SmartDashboard.putNumber("right cmd", signal.getRight());
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
        //TODO replace with vel config
        lMaster.set(ControlMode.PercentOutput, L);
        rMaster.set(ControlMode.PercentOutput, R);
    }

    @Override
    public synchronized void configureTalonsForAuto() {
        lMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 3, 0);
        rMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 3, 0);
    }

    @Override
    public synchronized void configureTalonsForTeleop() {
        lMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 50, 0);
        rMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 50, 0);
    }

    private void initTalons() {
        // constitutive features of this drive train
        // will only change if mechanical or electrical changes, and those are universal
        lSlave.set(ControlMode.Follower, lMaster.getDeviceID());
        rSlave.set(ControlMode.Follower, rMaster.getDeviceID());

        lMaster.setInverted(true);
        lSlave.setInverted(true);

        lMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        rMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

        lMaster.setSensorPhase(false);
        rMaster.setSensorPhase(false);

        lMaster.getSensorCollection().setQuadraturePosition(0, 0);
        rMaster.getSensorCollection().setQuadraturePosition(0, 0);
    }

    private Gyro getGyro() {
        return robotRegistry.get(Gyro.class);
    }

    private RobotState getRobotState() {
        return robotRegistry.get(RobotState.class);
    }
}
