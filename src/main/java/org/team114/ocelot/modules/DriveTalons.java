package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.util.Arrays;
import java.util.List;

public class DriveTalons {
    private TalonSRX leftMaster;
    private TalonSRX rightMaster;
    private TalonSRX leftSlave;
    private TalonSRX rightSlave;

    public DriveTalons(TalonSRX leftMaster, TalonSRX rightMaster, TalonSRX leftSlave, TalonSRX rightSlave) {
        this.leftMaster = leftMaster;
        this.rightMaster = rightMaster;
        this.leftSlave = leftSlave;
        this.rightSlave = rightSlave;

        configure();
    }

    private void configure() {
        leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
        rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());
        // TODO: configure the rest of the options
    }

    public TalonSRX getLeft() {
        return leftMaster;
    }

    public TalonSRX getRight() {
        return rightMaster;
    }

    public List<TalonSRX> getMasters() {
        return Arrays.asList(leftMaster, rightMaster);
    }
}
