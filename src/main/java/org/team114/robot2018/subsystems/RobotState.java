package org.team114.robot2018.subsystems;

import java.io.InterruptedIOException;
import java.util.Arrays;

import org.team114.robot2018.geometry.Point;
import org.team114.robot2018.modules.Gyro;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import org.team114.lib.subsystem.Subsystem;

public class RobotState implements Subsystem {

    public Pose currentPose = null;
    
    private TalonSRX leftMasterTalon, rightMasterTalon;
    private double lastLeftSpeed, lastRightSpeed;
    private double wheelbase_width;
    private double lastTimeStamp;
    private Gyro gyro;

    public class Pose {
        double x;
        double y;
        double radians;

        public Pose(double x, double y, double radians) {
            this.x = x;
            this.y = y;
            this.radians = radians;
        }
        
        public Point point() {
            return new Point(x, y);
        }

        public double angle() {
            return this.radians;
        }
        
        public double x() {
            return this.x;
        }

        public double y() {
            return this.y;
        }
    }

    public RobotState(TalonSRX leftMasterTalon, TalonSRX rightMasterTalon, double wheelbase_width) {
        this.rightMasterTalon = rightMasterTalon;
        this.leftMasterTalon = leftMasterTalon;
        this.wheelbase_width = wheelbase_width;
        gyro = new Gyro();
        while(gyro.isCalibrating());
        try {
            gyro.zeroYaw();
        } catch (InterruptedIOException e) {}
        while(gyro.isCalibrating());
    }
    
    public int leftEncoder() {
        return leftMasterTalon.getSelectedSensorPosition(0);
    }

    public void resetPosition() {
        currentPose = new Pose(0, 0, currentPose.radians);
    }
    
    public int rightEncoder() {
        return leftMasterTalon.getSelectedSensorPosition(0);
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
        double currYaw = 0;
        try {
            currYaw = Math.toRadians(gyro.getYaw());
        } catch (InterruptedIOException e) {}
        double vecAngle = (currYaw + currentPose.angle())/2;
        leftMasterTalon.setSelectedSensorPosition(0,0,0);
        double L = (double)(leftEncoder());
        double R = (double)(rightEncoder());
        double arcRadius = (wheelbase_width/2) * (L+R)/(L-R);
        double arcTheta = L / (arcRadius + (wheelbase_width/2));
        double vecDistance = arcRadius * Math.sin(arcTheta/2);

        currentPose = new Pose(currentPose.x() + vecDistance * Math.cos(vecAngle),
                currentPose.y() + vecDistance * Math.sin(vecAngle),
                currYaw);

        for (TalonSRX t : Arrays.asList(leftMasterTalon, rightMasterTalon)) {
            t.setSelectedSensorPosition(0,0,0);
        }

        //calculate accel
        double currentTime = timestamp;
        double lAccel = (lastLeftSpeed - leftMasterTalon.getSelectedSensorVelocity(0)) / (currentTime - lastTimeStamp);
        double rAccel = (lastRightSpeed - rightMasterTalon.getSelectedSensorVelocity(0)) / (currentTime - lastTimeStamp);

        lastLeftSpeed = leftMasterTalon.getSelectedSensorVelocity(0);
        lastRightSpeed = rightMasterTalon.getSelectedSensorVelocity(0);
        SmartDashboard.putNumber("Periodic Hz", 1/(currentTime - lastTimeStamp));
        lastTimeStamp = currentTime;

        SmartDashboard.putNumber("x EncoderTicks", currentPose.x());
        SmartDashboard.putNumber("y EncoderTicks", currentPose.y());
        SmartDashboard.putNumber("heading °", Math.toRadians(currentPose.angle()));

        SmartDashboard.putNumber("L Accel", lAccel);
        SmartDashboard.putNumber("R Accel", rAccel);
        SmartDashboard.putNumber("L Speed", lastLeftSpeed / (4096 * 2 * Math.PI * 0.1016));
        SmartDashboard.putNumber("R Speed", lastRightSpeed / (4096 * 2 * Math.PI * 0.1016));
    }
}
