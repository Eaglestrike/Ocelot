package org.team114.ocelot.modules;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;

public class Gyro {
    public static final Gyro shared = new Gyro();

    private final AHRS navx;
    private boolean isCalibrating;

    private Gyro() {
        navx = new AHRS(Port.kMXP);
        navx.zeroYaw();
        isCalibrating = true;
    }

    /**
     * @return the heading of the navx chip, in radians.
     */
    public double getYaw() {
        return Math.toRadians(navx.getYaw());
    }

    public void zeroYaw() {
        navx.zeroYaw();
    }

    // empty while block is deliberate, used for waiting
    @SuppressWarnings("StatementWithEmptyBody")
    public void waitUntilCalibrated() {
        while (isCalibrating()) {}
    }

    public boolean isCalibrating() {
        if (isCalibrating) {
            isCalibrating = navx.isCalibrating();
        }
        return isCalibrating;
    }
}
