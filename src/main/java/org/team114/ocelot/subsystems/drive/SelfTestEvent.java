package org.team114.ocelot.subsystems.drive;

/*
Test to make sure all Talon SRXs have a device ID between 0 and 62
 */
public final class SelfTestEvent extends DriveEvent {

    public SelfTestEvent() {
        super(SelfTestEvent.class.getCanonicalName());
    }
}
