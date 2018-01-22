package org.team114.ocelot.subsystems;

import java.io.InterruptedIOException;

import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.event.AbstractEvent;
import org.team114.ocelot.event.PubSub;
import org.team114.ocelot.modules.Gyro;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.util.Pose;

public class RobotState implements Subsystem {
    public final static class PoseEvent extends AbstractEvent {
        private Pose pose;

        private PoseEvent(Pose pose) {
            this.pose = pose;
        }

        public Pose getPose() {
            return pose;
        }
    }

    private Pose currentPose;

    private TalonSRX leftMasterTalon, rightMasterTalon;
    private double lastLeftSpeed, lastRightSpeed;
    private double wheelbase_width;
    private double lastTimeStamp;
    private Gyro gyro = Gyro.shared;

    public RobotState(TalonSRX leftMasterTalon, TalonSRX rightMasterTalon, double wheelbase_width) {
        this.rightMasterTalon = rightMasterTalon;
        this.leftMasterTalon = leftMasterTalon;
        this.wheelbase_width = wheelbase_width;
        gyro.waitUntilCalibrated();
        try {
            gyro.zeroYaw();
        } catch (InterruptedIOException e) {
            e.printStackTrace();
        }
    }

    public int leftEncoder() {
        return leftMasterTalon.getSelectedSensorPosition(0);
    }

    public int rightEncoder() {
        return rightMasterTalon.getSelectedSensorPosition(0);
    }

    public void resetPosition() {
        currentPose = new Pose(0, 0, currentPose.getHeading());
    }

    @Override
    public void onStart(double timestamp) {
        currentPose = new Pose(0, 0, 0);
        lastTimeStamp = timestamp;
    }

    @Override
    public void onStop(double timestamp) {
    }

    @Override
    public void onStep(double timestamp) {
        System.out.println("Step robot state: " + timestamp);

        double currYaw = 0;
        try {
            currYaw = Math.toRadians(gyro.getYaw());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        double vecAngle = (currYaw + currentPose.getHeading())/2;
        double vecDistance;
        double L = (double)(leftEncoder()) * Math.PI * 0.1016 / 4096 / 2;
        double R = (double)(rightEncoder()) * Math.PI * 0.1016 / 4096 / 2;

        leftMasterTalon.setSelectedSensorPosition(0, 0, 0);
        rightMasterTalon.setSelectedSensorPosition(0, 0, 0);
        if (true) {
          vecDistance = (L + R) / 2;
        } else if (L == R) {
            vecDistance = R;
        } else if (L == 0) {
            double arcTheta = R/wheelbase_width;
            vecDistance = (wheelbase_width/2) * Math.sin(arcTheta/2);
        } else if (R == 0) {
            double arcTheta = L/wheelbase_width;
            vecDistance = (wheelbase_width/2) * Math.sin(arcTheta/2);
        } else {
            double arcRadius = (wheelbase_width / 2) * (L + R) / (R - L);
            double arcTheta = L / (arcRadius + (wheelbase_width / 2));
            vecDistance = arcRadius * Math.sin(arcTheta / 2);
        }
        currentPose = new Pose(currentPose.getX() + vecDistance * Math.cos(vecAngle), currentPose.getY()
                + vecDistance * Math.sin(vecAngle), currYaw);

        //calculate accel
        double currentTime = timestamp;
        double lAccel = (lastLeftSpeed - L) / (currentTime - lastTimeStamp);
        double rAccel = (lastRightSpeed - R) / (currentTime - lastTimeStamp);

        lastLeftSpeed = leftMasterTalon.getSelectedSensorVelocity(0);
        lastRightSpeed = rightMasterTalon.getSelectedSensorVelocity(0);
        SmartDashboard.putNumber("Periodic Hz", 1/(currentTime - lastTimeStamp));
        lastTimeStamp = currentTime;

        SmartDashboard.putNumber("x", currentPose.getX());
        SmartDashboard.putNumber("y", currentPose.getY());
        SmartDashboard.putNumber("heading", Math.toDegrees(currentPose.getHeading()));

        SmartDashboard.putNumber("L Accel", lAccel);
        SmartDashboard.putNumber("R Accel", rAccel);
        SmartDashboard.putNumber("L Speed", lastLeftSpeed);
        SmartDashboard.putNumber("R Speed", lastRightSpeed);

        PubSub.shared.publish(new PoseEvent(currentPose));
    }
}
