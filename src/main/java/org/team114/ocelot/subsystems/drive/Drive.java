package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.event.EventQueue;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Drive implements Subsystem {

    private final EventQueue<DriveEvent> queue;
    private final Map<Class<? extends DriveEvent>, Consumer> handlerMap = new HashMap<>();
    private final Map<Side, DriveSideSettings> controlModeMap =
            new EnumMap<Side, DriveSideSettings>(Side.class);

    public Drive(DriveSideSettings leftSide, DriveSideSettings rightSide, EventQueue<DriveEvent> queue) {
        this.queue = queue;

        handlerMap.put(SelfTestEvent.class, selfTestEventHandler);
        handlerMap.put(SetNeutralModeEvent.class, setNeutralModeEventHandler);
        handlerMap.put(SetSideSpeedEvent.class, setSideSpeedEventHandler);
        handlerMap.put(SetControlModeEvent.class, setControlModeEventHandler);
        controlModeMap.put(Side.LEFT, leftSide);
        controlModeMap.put(Side.RIGHT, rightSide);
    }

    private void reset() {
        setControlModeEventHandler.accept(new SetControlModeEvent(Side.BOTH, ControlMode.PercentOutput));
        setSideSpeedEventHandler.accept(new SetSideSpeedEvent(0, 0));
    }

    public void onStart(double timestamp) {
        reset();
    }

    public void onStop(double timestamp) {
        reset();
    }

    @SuppressWarnings("unchecked")
    public void onStep(double timestamp) {
        // TODO: @aris @rebecca : need to pull more than 1 event. Otherwise there will be a lag
        // maybe pull for a certain amount of time. or until queue empty?
        // concerns: the drive monopolizing the thread if the queue is flooded.
        // queue may have to filter redundant events.
        DriveEvent next = queue.pull();
        handlerMap.get(next.getClass()).accept(next);
    }

    // MARK: Handlers

    private Consumer<SetNeutralModeEvent> setNeutralModeEventHandler = (event) -> {
        for (DriveSideSettings driveSideSettings : Drive.this.controlModeMap.values()) {
            driveSideSettings.setNeutralMode(event.neutralMode);
        }
    };

    private Consumer<SelfTestEvent> selfTestEventHandler = (event) -> {
        for (DriveSideSettings driveSideSettings : Drive.this.controlModeMap.values()) {
            // TODO: Publish an error event instead
            for (TalonSRX talon : driveSideSettings.getTalonSRXs()) {
                int id = talon.getDeviceID();
                if (id == 0) {
                    System.out.println("Talon " + id + " has not been configured.");
                } else if (id > 62 || id < 1) {
                    System.out.println("Talon " + id + " has an ID that is outside of ID bounds.");
                }
            }
        }
    };

    private Consumer<SetSideSpeedEvent> setSideSpeedEventHandler = (event) -> {
        DriveSideSettings left = this.controlModeMap.get(Side.LEFT);
        left.setSpeed(event.leftspeed);
        DriveSideSettings right = this.controlModeMap.get(Side.RIGHT);
        right.setSpeed(event.rightspeed);
    };

    private Consumer<SetControlModeEvent> setControlModeEventHandler = (event) -> {
        for (Side side : event.side) {
            this.controlModeMap.get(side).setControlMode(event.mode);
        }
    };

}
