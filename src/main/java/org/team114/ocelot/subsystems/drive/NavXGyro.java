package org.team114.ocelot.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class NavXGyro implements Gyro {

    private final AHRS navx;
    private boolean isCalibrating;

    @Inject
    NavXGyro() {
        navx = new AHRS(Port.kMXP);
        // we want the angle to read 90 after a zero yaw, but our reading is negated below, so the offset is -90.
        navx.setAngleAdjustment(-90.0);
        zeroYaw();
        isCalibrating = true;
    }

    private double boundDegrees(double angle) {
        return ((angle + 180) % 360 + 360) % 360 - 180;
    }

    @Override
    public void reset() {
        zeroYaw();
    }

    /**
     * Returns the heading according the gyroscope.
     * @return heading of the navx chip in radians
     */
    @Override
    public double getYaw() {
        return Math.toRadians(getYawDegrees());
    }

    @Override
    public double getYawDegrees() {
        // negated because the navx counts angles CW, we want CCW angles;
        return boundDegrees(-navx.getAngle());
    }

    @Override
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
