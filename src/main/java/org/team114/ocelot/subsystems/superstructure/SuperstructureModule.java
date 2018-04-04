package org.team114.ocelot.subsystems.superstructure;

import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;
import org.team114.ocelot.settings.Settings;

import static org.team114.ocelot.factory.TalonFactory.new775ProTalon;

@Module
public abstract class SuperstructureModule {

    @Provides
    static Carriage provideCarriage() {
        return new Carriage(
                new Solenoid(Settings.Carriage.INTAKE_CHANNEL),
                new Solenoid(Settings.Carriage.LIFT_STAGE_ONE),
                new Solenoid(Settings.Carriage.LIFT_STAGE_TWO),
                new775ProTalon(Settings.Carriage.LEFT_SPINNER),
                new775ProTalon(Settings.Carriage.RIGHT_SPINNER),
                new GP2YProximitySensor(new AnalogInput(Settings.DistanceSensor.CHANNEL)));
    }

    @Provides
    static Lift provideLift() {

        return new Lift(
                new775ProTalon(Settings.Lift.MASTER),
                new775ProTalon(Settings.Lift.SLAVE));
    }

}
