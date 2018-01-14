package org.team114.robot2018.subsystems;

import org.team114.robot2018.geometry.Point;
import org.team114.robot2018.modules.Gyro;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//import org.team114.lib.subsystem.Subsystem;

public class RobotState implements Subsystem {

    public Pose currentPose = null;
    
    @SuppressWarnings("unused")
    private TalonSRX right, left;
    private double wheelbase;
    private double lastTick;
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

    public RobotState(TalonSRX rightTalon, TalonSRX leftTalon, double wheelbase) {
        right = rightTalon;
        left = leftTalon;
        
        
        gyro = new Gyro();
        
        this.wheelbase = wheelbase;
    }

    @Override
    public void onStart(double timestamp) {
        currentPose = new Pose(0, 0, 0);
        lastTick = timestamp;
    }

    @Override
    public void onStop(double timestamp) {

    }

    @Override
    public void onStep(double timestamp) {
        double rightV = 0, leftV = 0; //Somehow get input from the Talon
        
        double right = rightV * (timestamp - lastTick);
        double left = leftV * (timestamp - lastTick);
        lastTick = timestamp;
        
        double distance = (right + left) / 2;
        double theta = (right - left) / wheelbase;
        
        //If gyro is not ready use dead-reckoning
        double angle = currentPose.angle() + theta;
        try {
            angle = gyro.getYaw();
        }catch(Exception e) {}
        
        //Apply
        theta = (theta + (angle - currentPose.angle())) / 2;
        
        currentPose = new Pose(currentPose.x() + distance * Math.cos(currentPose.angle() + theta / 2),
                currentPose.y() + distance * Math.sin(currentPose.angle() + theta / 2), angle);
    }
}
