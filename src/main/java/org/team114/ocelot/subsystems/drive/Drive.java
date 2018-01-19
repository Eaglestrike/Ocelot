package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.event.Event;
import org.team114.ocelot.event.EventQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Drive implements Subsystem {

    private final EventQueue queue;
    private final Map<Class, Consumer> handlerMap = new HashMap<>();
    private final TalonSRX[] talons;
//    private final Encoder encoder;

    public Drive(TalonSRX[] talons, EventQueue queue, int  encoderIdA, int encoderIdB) {
        this.talons = talons;
        this.queue = queue;

        handlerMap.put(SelfTestEvent.class, selfTestEventHandler);
        handlerMap.put(SetNeutralModeEvent.class, setNeutralModeEventHandler);
        handlerMap.put(SetSideSpeedEvent.class, setSideSpeedEventHandler);
    }

    public TalonSRX[] getTalons() {
        return talons;
    }

    public void onStart(double timestamp) { }
    public void onStop(double timestamp) { }

    @SuppressWarnings("unchecked")
    public void onStep(double timestamp) {
        Event next = queue.pull();
        handlerMap.get(next.getClass()).accept(next);
    }

    //Handlers below

    private Consumer<SetNeutralModeEvent> setNeutralModeEventHandler = (event) -> {
        TalonSRX[] talons = Drive.this.getTalons();
        for (TalonSRX talon : talons) {
            talon.setNeutralMode(event.neutralMode);
        }
    };


    private Consumer<SelfTestEvent> selfTestEventHandler = (event) -> {
        TalonSRX[] talons = Drive.this.getTalons();
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


    Consumer<SetSideSpeedEvent> setSideSpeedEventHandler = (event) -> {
        TalonSRX ltalon = Drive.this.getTalons()[0]; // assumes that the first item in the talons list is a left talon
        TalonSRX rtalon = Drive.this.getTalons()[1]; // assumes that the second item in the talons list is a right talon

        rtalon.set(event.mode, event.rightspeed);
        ltalon.set(event.mode, event.leftspeed);
    };
}
