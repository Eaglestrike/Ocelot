package org.team114.ocelot.modules;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;

public class Gyro {
    // having multiple gyro instances is dangerous
    public final static Gyro shared = new Gyro();

    private final AHRS navx;
    private boolean isCalibrating;

    private Gyro() {
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
     * Returns the heading according the gyroscope.
     * @return heading of the navx chip in radians
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
