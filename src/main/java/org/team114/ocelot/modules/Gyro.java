package org.team114.ocelot.modules;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;

public class Gyro {
    public static Gyro shared = new Gyro();

    private AHRS navx;
    private boolean isCalibrating;

    private Gyro() {
        navx = new AHRS(Port.kMXP);
        navx.setAngleAdjustment(-90.0);
        navx.zeroYaw();
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
        return boundDegrees(-navx.getAngle());
    }

    public void zeroYaw() {
        navx.zeroYaw();
    }

    public void waitUntilCalibrated() {
        while (isCalibrating());
    }

    public boolean isCalibrating() {
        if (isCalibrating) {
            isCalibrating = navx.isCalibrating();
        }
        return isCalibrating;
    }
}
