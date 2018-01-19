package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.event.Event;
import org.team114.ocelot.event.EventQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Drive implements Subsystem {

    private final EventQueue<DriveEvent> queue;
    private final Map<Class, Consumer> handlerMap = new HashMap<>();
    private final TalonSRX[] talons;

    public Drive(TalonSRX[] talons, EventQueue queue, int  encoderIdA, int encoderIdB) {
        this.talons = talons;
        this.queue = queue;

        handlerMap.put(SelfTestEvent.class, selfTestEventHandler);
        handlerMap.put(SetNeutralModeEvent.class, setNeutralModeEventHandler);
        handlerMap.put(SetSideSpeedEvent.class, setSideSpeedEventHandler);
    }

    private TalonSRX getRightTalon() {
        return talons[1]; //assumes second item is a right talon
    }

    private TalonSRX getLeftTalon() {
        return talons[0]; //assumes second item is a right talon
    }

    public void onStart(double timestamp) { }
    public void onStop(double timestamp) { }

    @SuppressWarnings("unchecked")
    public void onStep(double timestamp) {
        DriveEvent next = queue.pull();
        handlerMap.get(next.getClass()).accept(next);
    }

    //Handlers below

    private Consumer<SetNeutralModeEvent> setNeutralModeEventHandler = (event) -> {
        for (TalonSRX talon : Drive.this.talons) {
            talon.setNeutralMode(event.neutralMode);
        }
    };


    private Consumer<SelfTestEvent> selfTestEventHandler = (event) -> {
        TalonSRX[] talons = Drive.this.talons;
        for (TalonSRX talon: talons) {
            int id = talon.getDeviceID();
            if (id == 0) {
                System.out.println("Talon " + id + " has not been configured.");
            }
            else if (id > 62 || id < 1) {
                System.out.println("Talon " + id + " has an ID that is outside of ID bounds.");
            }
        }
    };


    private Consumer<SetSideSpeedEvent> setSideSpeedEventHandler = (event) -> {
        TalonSRX leftTalon = Drive.this.getRightTalon();
        TalonSRX rightTalon = Drive.this.getLeftTalon();

        rightTalon.set(event.mode, event.rightspeed);
        leftTalon.set(event.mode, event.leftspeed);
    };
}
