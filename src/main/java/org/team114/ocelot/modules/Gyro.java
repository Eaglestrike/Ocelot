package org.team114.ocelot.modules;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;

import java.io.InterruptedIOException;

public class Gyro {
    public static Gyro shared = new Gyro();

    private AHRS navx;
    private boolean isCalibrating;

    private Gyro() {
        navx = new AHRS(Port.kMXP);
        navx.zeroYaw();
        isCalibrating = true;
    }

    public double getYaw() throws InterruptedIOException {
        if (isCalibrating()) {
            throw new InterruptedIOException("The NavX is still calibrating, wait a bit!");
        }
        return navx.getYaw();
    }

    public void zeroYaw() throws InterruptedIOException {
        if (isCalibrating()) {
            throw new InterruptedIOException("The NavX is still calibrating, wait a bit!");
        }
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
