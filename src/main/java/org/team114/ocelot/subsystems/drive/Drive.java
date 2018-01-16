package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team114.ocelot.event.Event;
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

}
