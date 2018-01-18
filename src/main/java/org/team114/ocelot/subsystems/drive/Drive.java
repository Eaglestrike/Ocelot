package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team114.ocelot.event.Event;
import org.team114.ocelot.event.EventHandler;
import org.team114.ocelot.event.EventQueue;

import java.util.Map;

public class Drive implements Subsystem {

    private final EventQueue queue;
    private Map<Class, EventHandler> handlerMap;
    private final TalonSRX[] talons;
//    private final Encoder encoder;

    public Drive(TalonSRX[] talons, EventQueue queue, int  encoderIdA, int encoderIdB) {
        this.talons = talons;
        this.queue = queue;

        handlerMap.put(SelfTestEvent.class, new SelfTestEventHandler(this));
        handlerMap.put(SetNeutralModeEvent.class, new SetNeutralModeEventHandler(this));
    }

    public TalonSRX[] getTalons() {
        return talons;
    }

    public void onStart(double timestamp) { }
    public void onStop(double timestamp) { }

    public void onStep(double timestamp) {
        Event next = queue.poll();
        handlerMap.get(next.getClass()).handle(next);
    }

    //Handlers below

    class SetNeutralModeEventHandler implements EventHandler<SetNeutralModeEvent> {

        public void handle(SetNeutralModeEvent event) {
            TalonSRX[] talons = Drive.this.getTalons();
            for (TalonSRX talon: talons) {
                talon.setNeutralMode(event.neutralMode);
            }
        }
    }

    class SelfTestEventHandler implements EventHandler<SelfTestEvent> {

        public void handle(SelfTestEvent event) {
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
        }
    }

    class LeftDriveEventHandler implements EventHandler<SetSideSpeedEvent> {

        public void handle(SetSideSpeedEvent event) {
            TalonSRX ltalon = Drive.this.getTalons()[0]; // assumes that the first item in the talons list is a left talon
            TalonSRX rtalon = Drive.this.getTalons()[1]; // assumes that the second item in the talons list is a right talon

            rtalon.set(event.mode, event.rightspeed);
            ltalon.set(event.mode, event.leftspeed);
        }
    }



}
