package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Drive {

    private TalonSRX[] talons;
    private final Encoder encoder;


    public Drive(TalonSRX[] talons, EventQueue queue, int  encoderIdA, int encoderIdB) {
        this.talons = talons;
    }

    public TalonSRX[] getTalons() {
        return talons;
    }

}
