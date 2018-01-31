package org.team114.ocelot.modules;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;
import org.team114.ocelot.RobotRegistry;

public class Gyro {
    private final AHRS navx;
    private final RobotRegistry robotRegistry;
    private boolean isCalibrating;

    public Gyro(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
        navx = new AHRS(Port.kMXP);
        // we want the angle to read 90 after a zero yaw, but our reading is negated below, so the offset is -90.
        navx.setAngleAdjustment(-90.0);
        zeroYaw();
        isCalibrating = true;
    }

    private double boundDegrees(double angle) {
        return ((angle + 180) % 360 + 360) % 360 - 180;
    }

    /**
     * @return the heading of the navx chip modified to co-oridnate system conventions, in radians.
     */
    public double getYaw() {
        return Math.toRadians(getYawDegrees());
    }

    public double getYawDegrees() {
        // negated because the navx counts angles CW, we want CCW angles;
        return boundDegrees(-navx.getAngle());
    }

    public void init() {
        waitUntilCalibrated();
        zeroYaw();
    }
    private void zeroYaw() {
        navx.zeroYaw();
    }

    private void waitUntilCalibrated() {
        while (isCalibrating) {
            isCalibrating = navx.isCalibrating();
            if (isCalibrating) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
