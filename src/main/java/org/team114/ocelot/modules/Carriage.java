package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.*;

public class Carriage {
    Solenoid left;
    Solenoid right;
    Solenoid lift1;
    Solenoid lift2;
    Solenoid lift3;
    Solenoid lift4;
    Ultrasonic ultrasonic;


    public Carriage(int leftMechId, int rightMechId, int lift1Id, int lift2Id, int lift3Id, int lift4Id, int digitalInput, int digitalOutput) {
        left = new Solenoid(leftMechId);
        right = new Solenoid(rightMechId);
        lift1 = new Solenoid(lift1Id);
        lift2 = new Solenoid(lift2Id);
        lift3 = new Solenoid(lift3Id);
        lift4 = new Solenoid(lift4Id);
        ultrasonic = new Ultrasonic(digitalOutput, digitalInput);
    }

    public double getDistance(){
        double range = ultrasonic.getRangeInches();
        return range;
    }

    public void actuateIntake(boolean actuate) {
        left.set(actuate);
        right.set(actuate);
    }

    public void actuateFirstLift(boolean actuate) {
        lift1.set(actuate);
        lift2.set(actuate);
    }

    public void actuateSecondLift(boolean actuate) {
        lift3.set(actuate);
        lift4.set(actuate);
    }

}
