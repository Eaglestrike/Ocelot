package org.team114.ocelot.modules;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;
import org.team114.ocelot.RobotRegistry;

public class Gyro {
    private final AHRS navx;
    private boolean isCalibrating;

    public Gyro(RobotRegistry robotRegistry) {
        navx = new AHRS(Port.kMXP);
        // we want the angle to read 90 after a zero yaw, but our reading is negated below, so the offset is -90.
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
        // negated because the navx counts angles CW, we want CCW angles;
        return boundDegrees(-navx.getAngle());
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
