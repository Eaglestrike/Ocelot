package org.team114.ocelot.modules;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;

import java.io.InterruptedIOException;

public class Gyro {
    AHRS navx;
    boolean isCalibrating;

    public Gyro() {
        navx = new AHRS(Port.kMXP);
        navx.zeroYaw();
        isCalibrating = true;
    }

    public double getYaw() throws InterruptedIOException {
        if (!isCalibrating()) {
            return navx.getYaw();
        }
        throw new InterruptedIOException("The NavX is still calibrating, wait a bit!");
    }

    public void zeroYaw() throws InterruptedIOException {
        if (!isCalibrating()) {
            navx.zeroYaw();
        }
        throw new InterruptedIOException("The NavX is still calibrating, wait a bit!");
    }

    public boolean isCalibrating() {
        if (isCalibrating) {
            isCalibrating = navx.isCalibrating();
        }
        return isCalibrating;
    }
}
