package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.event.EventQueue;
import org.team114.ocelot.modules.DriveTalons;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Drive implements Subsystem {

    private final EventQueue<DriveEvent> queue;
    private final Map<Class<? extends DriveEvent>, Consumer> handlerMap = new HashMap<>();
    private final DriveTalons talons;

    public Drive(DriveTalons talons, EventQueue<DriveEvent> queue) {
        this.talons = talons;
        this.queue = queue;

        handlerMap.put(SelfTestEvent.class, selfTestEventHandler);
        handlerMap.put(SetNeutralModeEvent.class, setNeutralModeEventHandler);
        handlerMap.put(SetSideSpeedEvent.class, setSideSpeedEventHandler);
    }

    private void reset() {
        setSideSpeedEventHandler.accept(new SetSideSpeedEvent(ControlMode.PercentOutput, 0, 0));
    }

    public void onStart(double timestamp) {
        reset();
    }

    public void onStop(double timestamp) {
        reset();
    }

    @SuppressWarnings("unchecked")
    public void onStep(double timestamp) {
        DriveEvent next = queue.pull();
        handlerMap.get(next.getClass()).accept(next);
    }

    // MARK: Handlers

    private Consumer<SetNeutralModeEvent> setNeutralModeEventHandler = (event) -> {
        for (TalonSRX talon : Drive.this.talons.getMasters()) {
            talon.setNeutralMode(event.neutralMode);
        }
    };

    private Consumer<SelfTestEvent> selfTestEventHandler = (event) -> {
        for (TalonSRX talon : Drive.this.talons.getMasters()) {
            // TODO: Publish an error event instead
            int id = talon.getDeviceID();
            if (id == 0) {
                System.out.println("Talon " + id + " has not been configured.");
            } else if (id > 62 || id < 1) {
                System.out.println("Talon " + id + " has an ID that is outside of ID bounds.");
            }
        }
    };

    private Consumer<SetSideSpeedEvent> setSideSpeedEventHandler = (event) -> {
        Drive.this.talons.getLeft().set(event.mode, event.rightspeed);
        Drive.this.talons.getRight().set(event.mode, event.leftspeed);
    };
}
